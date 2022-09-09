package ca.homedepot.preference.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import ca.homedepot.preference.constants.SqlQueriesConstants;
import ca.homedepot.preference.dto.FileDTO;
import ca.homedepot.preference.repositories.entities.FileEntity;

@Repository
public interface FileRepo extends JpaRepository<FileEntity, Long>
{

	@Query(value = SqlQueriesConstants.SQL_INSERT_HDPC_FILE, nativeQuery = true)
	void insert(FileDTO file);
}
