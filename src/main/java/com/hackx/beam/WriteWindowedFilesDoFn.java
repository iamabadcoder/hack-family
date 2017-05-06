package com.hackx.beam;

import com.google.common.annotations.VisibleForTesting;
import org.apache.beam.sdk.coders.Coder;
import org.apache.beam.sdk.coders.StringUtf8Coder;
import org.apache.beam.sdk.transforms.DoFn;
import org.apache.beam.sdk.transforms.windowing.IntervalWindow;
import org.apache.beam.sdk.util.IOChannelFactory;
import org.apache.beam.sdk.util.IOChannelUtils;
import org.apache.beam.sdk.values.KV;
import org.joda.time.format.DateTimeFormatter;
import org.joda.time.format.ISODateTimeFormat;

import java.io.OutputStream;
import java.nio.channels.Channels;
import java.nio.charset.StandardCharsets;

/**
 * A {@link DoFn} that writes elements to files with names deterministically derived from the lower
 * and upper bounds of their key (an {@link IntervalWindow}).
 * <p>
 * <p>This is test utility code, not for end-users, so examples can be focused
 * on their primary lessons.
 */
public class WriteWindowedFilesDoFn
        extends DoFn<KV<IntervalWindow, Iterable<KV<String, Long>>>, Void> {

    static final byte[] NEWLINE = "\n".getBytes(StandardCharsets.UTF_8);
    static final Coder<String> STRING_CODER = StringUtf8Coder.of();

    private static DateTimeFormatter formatter = ISODateTimeFormat.hourMinute();

    private final String output;

    public WriteWindowedFilesDoFn(String output) {
        this.output = output;
    }

    @VisibleForTesting
    public static String fileForWindow(String output, IntervalWindow window) {
        return String.format(
                "%s-%s-%s", output, formatter.print(window.start()), formatter.print(window.end()));
    }

    @ProcessElement
    public void processElement(ProcessContext context) throws Exception {
        // Build a file name from the window
        IntervalWindow window = context.element().getKey();
        String outputShard = fileForWindow(output, window);

        // Open the file and write all the values
        IOChannelFactory factory = IOChannelUtils.getFactory(outputShard);
        OutputStream out = Channels.newOutputStream(factory.create(outputShard, "text/plain"));
        for (KV<String, Long> wordCount : context.element().getValue()) {
            STRING_CODER.encode(
                    wordCount.getKey() + ": " + wordCount.getValue(), out, Coder.Context.OUTER);
            out.write(NEWLINE);
        }
        out.close();
    }
}
