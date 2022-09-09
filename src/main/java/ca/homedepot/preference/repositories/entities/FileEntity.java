package ca.homedepot.preference.repositories.entities;

import lombok.Data;

import javax.persistence.*;
import java.math.BigDecimal;
import java.util.Date;

@Entity
@Data
@Table(name = "hdpc_file", schema = "pcam")
public class FileEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "file_id")
    private Long fileId;

    /**
     * The article id.
     */
    @Column(name = "file_name")
    private String fileName;

    @ManyToOne
    @JoinColumn(name = "job_id")
    private JobEntity job;

    /**
     * The status.
     */
    @Column(name = "status")
    private String status;

    @Column(name = "file_source_id")
    private BigDecimal fileSourceId;

    /**
     * The Phone number.
     */
    @Column(name = "start_time")
    private Date startTime;

    /**
     * The notification type.
     */
    @Column(name = "end_time")
    private Date endTime;

    @Column(name = "inserted_by")
    private String insertedBy;

    @Column(name = "inserted_date")
    private Date insertedDate;

    @Column(name = "updated_by")
    private String updatedBy;

    @Column(name = "updated_date")
    private Date updatedDate;
}
