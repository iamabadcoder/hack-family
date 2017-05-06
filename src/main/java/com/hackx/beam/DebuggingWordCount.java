package com.hackx.beam;

import org.apache.beam.sdk.Pipeline;
import org.apache.beam.sdk.io.TextIO;
import org.apache.beam.sdk.metrics.Counter;
import org.apache.beam.sdk.metrics.Metrics;
import org.apache.beam.sdk.options.Default;
import org.apache.beam.sdk.options.Description;
import org.apache.beam.sdk.options.PipelineOptionsFactory;
import org.apache.beam.sdk.testing.PAssert;
import org.apache.beam.sdk.transforms.DoFn;
import org.apache.beam.sdk.transforms.ParDo;
import org.apache.beam.sdk.values.KV;
import org.apache.beam.sdk.values.PCollection;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Arrays;
import java.util.List;
import java.util.regex.Pattern;

public class DebuggingWordCount {

    public static void main(String[] args) {
        WordCountOptions options = PipelineOptionsFactory.fromArgs(args).withValidation()
                .as(WordCountOptions.class);
        Pipeline p = Pipeline.create(options);

        PCollection<KV<String, Long>> filteredWords =
                p.apply("ReadLines", TextIO.Read.from(options.getInputFile()))
                        .apply(new WordCount.CountWords())
                        .apply(ParDo.of(new FilterTextFn(options.getFilterPattern())));

        /**
         * Concept #3: PAssert is a set of convenient PTransforms in the style of
         * Hamcrest's collection matchers that can be used when writing Pipeline level tests
         * to validate the contents of PCollections. PAssert is best used in unit tests
         * with small data sets but is demonstrated here as a teaching tool.
         *
         * <p>Below we verify that the set of filtered words matches our expected counts. Note
         * that PAssert does not provide any output and that successful completion of the
         * Pipeline implies that the expectations were met. Learn more at
         * https://beam.apache.org/documentation/pipelines/test-your-pipeline/ on how to test
         * your Pipeline and see {@link DebuggingWordCountTest} for an example unit test.
         */
        List<KV<String, Long>> expectedResults = Arrays.asList(
                KV.of("Flourish", 3L),
                KV.of("stomach", 1L));
        PAssert.that(filteredWords).containsInAnyOrder(expectedResults);

        p.run().waitUntilFinish();
    }

    /**
     * Options supported by {@link DebuggingWordCount}.
     * <p>
     * <p>Inherits standard configuration options and all options defined in
     * {@link WordCount.WordCountOptions}.
     */
    public interface WordCountOptions extends WordCount.WordCountOptions {

        @Description("Regex filter pattern to use in DebuggingWordCount. "
                + "Only words matching this pattern will be counted.")
        @Default.String("Flourish|stomach")
        String getFilterPattern();

        void setFilterPattern(String value);
    }

    /**
     * A DoFn that filters for a specific key based upon a regular expression.
     */
    public static class FilterTextFn extends DoFn<KV<String, Long>, KV<String, Long>> {
        /**
         * Concept #1: The logger below uses the fully qualified class name of FilterTextFn as the
         * logger. Depending on your SLF4J configuration, log statements will likely be qualified by
         * this name.
         * <p>
         * <p>Note that this is entirely standard SLF4J usage. Some runners may provide a default SLF4J
         * configuration that is most appropriate for their logging integration.
         */
        private static final Logger LOG = LoggerFactory.getLogger(FilterTextFn.class);

        private final Pattern filter;
        /**
         * Concept #2: A custom metric can track values in your pipeline as it runs. Each
         * runner provides varying levels of support for metrics, and may expose them
         * in a dashboard, etc.
         */
        private final Counter matchedWords = Metrics.counter(FilterTextFn.class, "matchedWords");
        private final Counter unmatchedWords = Metrics.counter(FilterTextFn.class, "unmatchedWords");

        public FilterTextFn(String pattern) {
            filter = Pattern.compile(pattern);
        }

        @ProcessElement
        public void processElement(ProcessContext c) {
            if (filter.matcher(c.element().getKey()).matches()) {
                // Log at the "DEBUG" level each element that we match. When executing this pipeline
                // these log lines will appear only if the log level is set to "DEBUG" or lower.
                LOG.debug("Matched: " + c.element().getKey());
                matchedWords.inc();
                c.output(c.element());
            } else {
                // Log at the "TRACE" level each element that is not matched. Different log levels
                // can be used to control the verbosity of logging providing an effective mechanism
                // to filter less important information.
                LOG.trace("Did not match: " + c.element().getKey());
                unmatchedWords.inc();
            }
        }
    }

}
