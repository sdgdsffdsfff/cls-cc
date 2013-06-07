/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package cn.ac.iie.cls.cc.slavecc;

import cn.ac.iie.cls.cc.slavecc.clsagent.CLSAgentDataCollectHandler;
import cn.ac.iie.cls.cc.slavecc.clsagent.CLSAgentDownloadHandler;
import cn.ac.iie.cls.cc.slavecc.clsagent.CLSAgentReportHandler;
import cn.ac.iie.cls.cc.slavecc.clsagent.ClsAgentStatusHandler;
import cn.ac.iie.cls.cc.slavecc.datacachecluster.DataCacheClusterStatusHandler;
import cn.ac.iie.cls.cc.slavecc.datadispatch.DataDispatchExecuteHandler;
import cn.ac.iie.cls.cc.slavecc.dataetl.DataETLCheckStatusHandler;
import cn.ac.iie.cls.cc.slavecc.dataetl.DataETLExecuteHandler;
import cn.ac.iie.cls.cc.slavecc.dataetl.DataETLTaskReportHandler;
import cn.ac.iie.cls.cc.slavecc.dataetlcluster.DataEtlClusterStatusHandler;
import cn.ac.iie.cls.cc.slavecc.linkagecmd.ConfigExecuteHandler;
import cn.ac.iie.cls.cc.slavecc.linkagecmd.QueryExecuteHandler;
import cn.ac.iie.cls.cc.slavecc.nosqlcluster.NoSqlClusterSessionHandler;
import cn.ac.iie.cls.cc.slavecc.nosqlcluster.NoSqlClusterStatusHandler;
import cn.ac.iie.cls.cc.slavecc.raccluster.RacClusterSessionHandler;
import cn.ac.iie.cls.cc.slavecc.raccluster.RacClusterStatusHandler;
import java.util.HashMap;
import java.util.Map;
import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;

/**
 *
 * @author alexmu
 */
public class SlaveHandlerFactory {

    static Logger logger = null;

    static {
        PropertyConfigurator.configure("log4j.properties");
        logger = Logger.getLogger(SlaveHandlerFactory.class.getName());
    }
    private static Map<String, Class> SlaveClassSet = new HashMap<String, Class>() {
        {
            //dataetl
            put("/dataetl/execute", DataETLExecuteHandler.class);
            put("/dataetl/checkstatus", DataETLCheckStatusHandler.class);
            put("/dataetl/etltaskreport", DataETLTaskReportHandler.class);
            //linkagecmd
            put("/linkagecmd/query/execute", QueryExecuteHandler.class);
            put("/linkagecmd/config/execute", ConfigExecuteHandler.class);
            //datadispatch
            put("/datadispatch/execute", DataDispatchExecuteHandler.class);
            //clsagent
            put("/clsagent/download", CLSAgentDownloadHandler.class);
            put("/clsagent/datacollect", CLSAgentDataCollectHandler.class);
            put("/clsagent/status", ClsAgentStatusHandler.class);
            put("/clsagent/report", CLSAgentReportHandler.class);
            //dataetlcluster
            put("/dataetlcluster/status", DataEtlClusterStatusHandler.class);
            //datacachecluster
            put("/datacachecluster/status", DataCacheClusterStatusHandler.class);
            //nosqlcluster
            put("/nosqlcluster/status", NoSqlClusterStatusHandler.class);
            put("/nosqlcluster/session", NoSqlClusterSessionHandler.class);
            //raccluster
            put("/raccluster/status", RacClusterStatusHandler.class);
            put("/raccluster/session", RacClusterSessionHandler.class);
        }
    };

    public static SlaveHandler getSlaveHandler(String pRequestPath) throws Exception {
        SlaveHandler slaveHandler = null;

        Class slaveHandlerClass = SlaveClassSet.get(pRequestPath);
        if (slaveHandlerClass != null) {
            try {
                slaveHandler = (SlaveHandler) (slaveHandlerClass.newInstance());
            } catch (Exception ex) {
                slaveHandler = null;
                throw new Exception("initializing slave handler for " + pRequestPath + " is failed for " + ex.getMessage(), ex);
            }
        } else {
            throw new Exception("no slave handler for " + pRequestPath + " is found ");
        }
        return slaveHandler;
    }
}