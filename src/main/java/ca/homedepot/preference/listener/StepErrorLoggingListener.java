package ca.homedepot.preference.listener;

import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.ExitStatus;
import org.springframework.batch.core.StepExecution;
import org.springframework.batch.core.StepExecutionListener;

import java.util.List;

@Slf4j
public class StepErrorLoggingListener implements StepExecutionListener {
    @Override
    public void beforeStep(StepExecution stepExecution) {

    }

    @Override
    public ExitStatus afterStep(StepExecution stepExecution) {
        List<Throwable> exceptions = stepExecution.getFailureExceptions();

        if(exceptions.isEmpty()){
            return ExitStatus.COMPLETED;
        }
        log.info(" The step: {} has {} erros. ",stepExecution.getStepName() , exceptions.size());
        exceptions.forEach(ex -> log.info(" Exception has ocurred:  " + ex.getMessage()));

        return ExitStatus.FAILED;
    }
}
