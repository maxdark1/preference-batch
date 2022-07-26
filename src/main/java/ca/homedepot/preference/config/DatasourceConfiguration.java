package ca.homedepot.preference.config;

import javax.persistence.EntityManagerFactory;
import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.jdbc.DataSourceProperties;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.orm.jpa.EntityManagerFactoryBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.transaction.PlatformTransactionManager;


/**
 * Data source configuration for back in stock schema and enable JPA transactions for associated repository and
 * entities.
 */
@Configuration
@EnableJpaRepositories(entityManagerFactoryRef = DatasourceConfiguration.ENTITYMANAGERFACTORY, transactionManagerRef = DatasourceConfiguration.TRANSACTIONMANAGER, basePackages = DatasourceConfiguration.REPOSITORY)
public class DatasourceConfiguration
{

	/**
	 * The Datasource.
	 */
	private static final String DATASOURCE = "preferenceCentre";
	/**
	 * The Entitymanagerfactory.
	 */
	public static final String ENTITYMANAGERFACTORY = "preferenceCentreManagerFactory";
	/**
	 * The Transactionmanager.
	 */
	public static final String TRANSACTIONMANAGER = "preferenceCentreTransactionManager";
	/**
	 * The Model.
	 */
	private static final String MODEL = "ca.homedepot.preferencecentre.repositories.entities";
	/**
	 * The Repository.
	 */
	public static final String REPOSITORY = "ca.homedepot.preferencecentre.repositories";

	/**
	 * Method to build a data source pointing to preference centre batch schema
	 *
	 * @return Data source
	 */
	@Bean(name = DATASOURCE)
	@Primary
	@ConfigurationProperties("spring.datasource")
	public DataSource dataSource()
	{
		return dataSourceProperties().initializeDataSourceBuilder().build();
	}

	/**
	 * Method to return the data source properties of preference centre batch schema.
	 *
	 * @return DataSourceProperties. data source properties
	 */
	@Bean
	@Primary
	@ConfigurationProperties("spring.datasource")
	public DataSourceProperties dataSourceProperties()
	{
		return new DataSourceProperties();
	}

	/**
	 * Entity manager factory local container entity manager factory bean.
	 *
	 * @param builder
	 *           the builder
	 * @param dataSource
	 *           the data source
	 * @return EntityManagerFactory local container entity manager factory bean
	 */
	@Bean(name = ENTITYMANAGERFACTORY)
	public LocalContainerEntityManagerFactoryBean entityManagerFactory(final EntityManagerFactoryBuilder builder,
			final @Qualifier(DATASOURCE) DataSource dataSource)
	{
		return builder.dataSource(dataSource).packages(MODEL).persistenceUnit(DATASOURCE).build();
	}

	/**
	 * Transaction manager platform transaction manager.
	 *
	 * @param entityManagerFactory
	 *           the entity manager factory
	 * @return platform transaction manager
	 */
	@Bean(name = TRANSACTIONMANAGER)
	public PlatformTransactionManager transactionManager(
			@Qualifier(ENTITYMANAGERFACTORY) EntityManagerFactory entityManagerFactory)
	{
		return new JpaTransactionManager(entityManagerFactory);
	}

}
