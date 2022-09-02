package ca.homedepot.preference.listener;

import ca.homedepot.preference.constants.SqlQueriesConstants;
import ca.homedepot.preference.dto.Job;
import ca.homedepot.preference.model.InboundRegistration;
import ca.homedepot.preference.repositories.JobRepo;
import ca.homedepot.preference.repositories.entities.JobEntity;
import ca.homedepot.preference.service.PreferenceService;
import lombok.RequiredArgsConstructor;
import org.springframework.batch.core.ItemReadListener;
import org.springframework.batch.item.database.BeanPropertyItemSqlParameterSourceProvider;
import org.springframework.batch.item.database.JdbcBatchItemWriter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

import javax.sql.DataSource;
import java.util.Date;
import java.util.List;

public class RegistrationItemReaderListener implements ItemReadListener<InboundRegistration> {

    private DataSource dataSource;

    private static final String JOB_NAME_REGISTRATION_INBOUND = "registrationInbound";


    private PreferenceService preferenceService;

    public void setPreferenceService(PreferenceService preferenceService) {
        this.preferenceService = preferenceService;
    }

    public void setDataSource(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    @Override
    public void beforeRead() {
        Job job = new Job();

        job.setJob_name(JOB_NAME_REGISTRATION_INBOUND);
        job.setStart_time(new Date());
        job.setStatus(true);
        job.setInserted_date(new Date());
        job.setInserted_by("BATCH");

        try {
          // JobEntity jobEntity = preferenceService.saveJob(job);

           // System.out.println(jobEntity.toString());
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void afterRead(InboundRegistration item) {

    }

    @Override
    public void onReadError(Exception ex) {

    }

    @Bean
    public JdbcBatchItemWriter<Job> jobWriter(){
        JdbcBatchItemWriter<Job> writer = new JdbcBatchItemWriter<>();

        writer.setDataSource(dataSource);
        writer.setSql(SqlQueriesConstants.SQL_INSERT_HDPC_JOB);
        writer.setItemSqlParameterSourceProvider(new BeanPropertyItemSqlParameterSourceProvider<Job>());

        return writer;
    }

}
