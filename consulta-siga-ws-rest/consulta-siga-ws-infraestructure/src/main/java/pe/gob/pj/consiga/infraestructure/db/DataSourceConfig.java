package pe.gob.pj.consiga.infraestructure.db;

import java.io.IOException;
import java.util.Properties;

import javax.naming.NamingException;
import javax.sql.DataSource;

import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jndi.JndiObjectFactoryBean;
import org.springframework.orm.hibernate5.HibernateTransactionManager;
import org.springframework.orm.hibernate5.LocalSessionFactoryBean;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import pe.gob.pj.consiga.domain.utils.ProjectProperties;

@Configuration
@EnableTransactionManagement
public class DataSourceConfig {

	
	private static Properties getHibernatePropertiesPostgresql() {
		Properties hibernateProperties = new Properties();
		hibernateProperties.put("hibernate.dialect", "org.hibernate.dialect.PostgreSQL92Dialect");
		hibernateProperties.put("hibernate.show_sql", false);
		return hibernateProperties;
	}
	
	private static Properties getHibernatePropertiesOracle() {
		Properties hibernateProperties = new Properties();
		hibernateProperties.put("hibernate.dialect", "org.hibernate.dialect.Oracle10gDialect");
		hibernateProperties.put("hibernate.show_sql", false);
		return hibernateProperties;
	}
	
	/* Creación de conexión con base de datos seguridad */
	@Bean(name = "cxSeguridadDS")
	public DataSource jndiConexionSeguridad() throws IllegalArgumentException, NamingException {
		JndiObjectFactoryBean bean = new JndiObjectFactoryBean();
		bean.setJndiName("java:jboss/datasources/consulta-sigaWSSeguridad");
		bean.setProxyInterface(DataSource.class);
		bean.setLookupOnStartup(false);
		bean.setCache(true);
		bean.afterPropertiesSet();
		return (DataSource) bean.getObject();
	}	
		
	@Bean(name = "sessionSeguridad")
	public SessionFactory getSessionFactorySeguridad(@Qualifier("cxSeguridadDS") DataSource seguridadDS) throws IOException {
		LocalSessionFactoryBean sessionFactoryBean = new LocalSessionFactoryBean();
		sessionFactoryBean.setPackagesToScan("pe.gob.pj.consiga.infraestructure.db.entity.security");
		sessionFactoryBean.setHibernateProperties(getHibernatePropertiesPostgresql());
		sessionFactoryBean.setDataSource(seguridadDS);
		sessionFactoryBean.afterPropertiesSet();
		return sessionFactoryBean.getObject();
	}

	@Bean(name = "txManagerSeguridad")
	public HibernateTransactionManager getTransactionManagerSeguridad(@Qualifier("sessionSeguridad") SessionFactory sessionSeguridad) throws IOException {
		HibernateTransactionManager transactionManager = new HibernateTransactionManager();
		transactionManager.setSessionFactory(sessionSeguridad);
		transactionManager.setDefaultTimeout(ProjectProperties.getTimeoutBdTransactionSegundos());
		return transactionManager;
	}
	
	/* Creación de conexión con base de datos SIGA*/
	@Bean(name = "cxSigaDS")
	public DataSource jndiConexionSiga() throws IllegalArgumentException, NamingException {
		JndiObjectFactoryBean bean = new JndiObjectFactoryBean();
		bean.setJndiName("java:jboss/datasources/consulta-sigaWSSiga");
		bean.setProxyInterface(DataSource.class);
		bean.setLookupOnStartup(false);
		bean.setCache(true);
		bean.afterPropertiesSet();
		return (DataSource) bean.getObject();
	}	
		
	@Bean(name = "sessionSiga")
	public SessionFactory getSessionFactorySiga(@Qualifier("cxSigaDS") DataSource cxSigaDS) throws IOException {
		LocalSessionFactoryBean sessionFactoryBean = new LocalSessionFactoryBean();
		sessionFactoryBean.setPackagesToScan("pe.gob.pj.consiga.infraestructure.db.entity.siga");
		sessionFactoryBean.setHibernateProperties(getHibernatePropertiesOracle());
		sessionFactoryBean.setDataSource(cxSigaDS);
		sessionFactoryBean.afterPropertiesSet();
		return sessionFactoryBean.getObject();
	}

	@Bean(name = "txManagerSiga")
	public HibernateTransactionManager getTransactionManagerSiga(@Qualifier("sessionSiga") SessionFactory sessionSiga) throws IOException {
		HibernateTransactionManager transactionManager = new HibernateTransactionManager();
		transactionManager.setSessionFactory(sessionSiga);
		transactionManager.setDefaultTimeout(ProjectProperties.getTimeoutBdTransactionSegundos());
		return transactionManager;
	}
	
	
}
