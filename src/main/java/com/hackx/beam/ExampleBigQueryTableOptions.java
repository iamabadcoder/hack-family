package com.hackx.beam;

import com.google.api.services.bigquery.model.TableSchema;
import org.apache.beam.sdk.options.*;

/**
 * Options that can be used to configure BigQuery tables in Beam examples.
 * The project defaults to the project being used to run the example.
 */
public interface ExampleBigQueryTableOptions extends GcpOptions {
    @Description("BigQuery dataset name")
    @Default.String("beam_examples")
    String getBigQueryDataset();

    void setBigQueryDataset(String dataset);

    @Description("BigQuery table name")
    @Default.InstanceFactory(BigQueryTableFactory.class)
    String getBigQueryTable();

    void setBigQueryTable(String table);

    @Description("BigQuery table schema")
    TableSchema getBigQuerySchema();

    void setBigQuerySchema(TableSchema schema);

    /**
     * Returns the job name as the default BigQuery table name.
     */
    class BigQueryTableFactory implements DefaultValueFactory<String> {
        @Override
        public String create(PipelineOptions options) {
            return options.getJobName().replace('-', '_');
        }
    }
}
