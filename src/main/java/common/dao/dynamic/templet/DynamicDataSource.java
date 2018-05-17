package common.dao.dynamic.templet;

import org.springframework.jdbc.datasource.lookup.AbstractRoutingDataSource;

 
public class DynamicDataSource  extends AbstractRoutingDataSource{
	private static org.slf4j.Logger log = org.slf4j.LoggerFactory.getLogger(DynamicDataSource.class);

	@Override
	protected Object determineCurrentLookupKey() {
		// TODO Auto-generated method stub
		log.info("连接数据源"+DataSourceHolder.getDataSource());
	    return DataSourceHolder.getDataSource();
	}

}
