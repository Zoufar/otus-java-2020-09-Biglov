package hw21cache;

import org.flywaydb.core.Flyway;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import hw21cache.cachehw.MyCache;
import hw21cache.cachehw.HwListener;
import hw21cache.cachehw.HwCache;

import hw21cache.core.model.Account;
import hw21cache.core.model.Client;
import hw21cache.core.service.DBServiceMapperImpl;
import hw21cache.demo.DataSourceCache;
import hw21cache.jdbc.DbExecutor;
import hw21cache.jdbc.DbExecutorImpl;
import hw21cache.jdbc.mapper.EntitySQLMetaData;
import hw21cache.jdbc.mapper.EntitySQLMetaDataImpl;
import hw21cache.jdbc.mapper.JdbcMapper;
import hw21cache.jdbc.mapper.JdbcMapperImpl;
import hw21cache.jdbc.sessionmanager.SessionManagerJdbc;

import java.util.ArrayList;
import java.util.stream.Collectors;
import java.util.stream.IntStream;


    public class HWCacheDemo {
    private static final Logger logger = LoggerFactory.getLogger(HWCacheDemo.class);

    private DataSourceCache dataSource = new DataSourceCache();
    private SessionManagerJdbc sessionManager = new SessionManagerJdbc(dataSource);

    private DbExecutor <Client> dbExecutor = new DbExecutorImpl<>();
    private JdbcMapper<Client> jdbcMapperClient = new JdbcMapperImpl<>(sessionManager, dbExecutor);
    private JdbcMapper<Account> jdbcMapperAccount = new JdbcMapperImpl<>(sessionManager, dbExecutor);
    private EntitySQLMetaData <Client> sqlMetaDataClient = new EntitySQLMetaDataImpl<>(Client.class);
    private EntitySQLMetaData <Account> sqlMetaDataAccount = new EntitySQLMetaDataImpl<>(Account.class);

    private DBServiceMapperImpl dbServiceMapperCli = new DBServiceMapperImpl(jdbcMapperClient);

    private HwCache<Long, Client> cacheDB = new MyCache<>();
    private static int numberCacheReads = 0;



    public static void main(String[] args) throws InterruptedException {

        new HWCacheDemo().demo1();
        new HWCacheDemo().demo2();

    }

    private void demo1() {
        HwCache<Integer, Integer> cache = new MyCache<>();

        HwListener<Integer, Integer> listener = new HwListener<Integer, Integer>() {
            @Override
            public void notify(Integer key, Integer value, String action) {
                logger.info("key:{}, value:{}, action: {}", key, value, action);
            }
        };

        cache.addListener(listener);
        cache.put(1, 100);

        logger.info("getValue:{}", cache.get(1));
        cache.remove(1);
        cache.removeListener(listener);
        logger.info("List of listeners size after removal is {}", cache.sizeListenersList());
    }

    private void demo2() throws InterruptedException {


        HwListener<Long, Client> listenerDB = new HwListener<Long, Client>() {
            @Override
            public void notify(Long key, Client value, String action) {

                logger.info("key:{}, value:{}, action: {}", key, value, action);
            }
        };
        HwListener<Long, Client> listenerCount = new HwListener<Long, Client>() {
            @Override
            public void notify(Long key, Client value, String action) {
                numberCacheReads = action.equals("get")? numberCacheReads+1 : numberCacheReads;
            }
        };

        cacheDB.addListener(listenerDB);
        cacheDB.addListener(listenerCount);
        cacheDB.put(1000L, new Client(1000L, "dbServiceClient",50));

        logger.info("getValue:{}", cacheDB.get(1000L));
        cacheDB.remove(1000L);

        flywayMigrations(dataSource);

        int n = 100;  // number of values stored in cache and DB

        IntStream.rangeClosed(1,2*n).boxed().map(idx ->createClient(idx)).
                forEach( cli -> dbServiceMapperCli.saveObject(cli));

        IntStream.rangeClosed(1,n).boxed().map(idx -> createClient(idx)).
                forEach( cli -> {cacheDB.put((Long)cli.getId(), cli);
                    //                           dbServiceMapperCli.saveObject(cli);
                });
        logger.info("cache size immediately after filling is {}", cacheDB.size());

        numberCacheReads = 0;
        logger.info("starting reading {} values presumably from cache", n);
        var listClients = (ArrayList<Client>) IntStream.rangeClosed(1,n).boxed().map(idx -> read(idx))
                         .collect(Collectors.toList());
        logger.info("finished reading {} values, number of values read  from cache: {}", n, numberCacheReads);

        logger.info("starting reading from DB");
        IntStream.rangeClosed(n+1,2*n).boxed().map( idx -> read(idx))
                                  .forEach( cli -> listClients.add(cli));
        logger.info("finished reading {} values from DB", n);

        logger.info("cache size immediately after reading is {}", cacheDB.size());
        System.gc();
        Thread.sleep(100L);
        logger.info("cache size immediately after GC is {}", cacheDB.size());

        logger.info("List of listeners size is {}", cacheDB.sizeListenersList());
        // checking if listenerDB works
        cacheDB.put(1000L, new Client(1000L, "Client A",50));

        listenerDB = null;
        // checking if listenerDB works immediately after nulling of main reference
        cacheDB.put(500L, new Client(500L, "Client after nulling",30));

        System.gc();
        Thread.sleep(100L);
        // // checking if listenerDB works after GC
        cacheDB.put(700L, new Client(700L, "Client after GC",40));
        logger.info("List of listeners size after null+ GC is {}", cacheDB.sizeListenersList());

        cacheDB.removeListener(listenerCount);
        logger.info("List of listeners size after one removal is {}", cacheDB.sizeListenersList());

        cacheDB.removeListener(listenerDB);
        logger.info("List of listeners size after null removal is {}", cacheDB.sizeListenersList());

    }

    private Client createClient(int idx){
        return new Client(idx, "client no."+idx, idx);
    }

    private Client read(int idx){
        if(cacheDB.containsKey((long)idx)){
            return cacheDB.get((long)idx);
        }
        return (Client) dbServiceMapperCli.getObject((long)idx,Client.class);
    }


    private static void flywayMigrations(javax.sql.DataSource dataSource) {
            logger.info("db migration started...");
            var flyway = Flyway.configure()
                    .dataSource(dataSource)
                    .locations("classpath:/db/migration")
                    .load();
            flyway.migrate();
            logger.info("db migration finished.");
            logger.info("***");
    }

    }
