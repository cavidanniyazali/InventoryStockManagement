# Inventory Stock Management
If you want to download and run the project, you are likely to get an error. This is due to the data.sql file under the src/main/resources folder. Except for 
create table if not exists persistent_logins ( 
  username varchar(100) not null, 
  series varchar(64) primary key, 
  token varchar(64) not null, 
  last_used timestamp not null
);
codes in the file, you need to delete or comment the lines at the bottom before the first run, and reactivate them in your next runs. Because an initialization process is made here, it cannot find the tables in the first run, but in the next runs, the tables are not overwritten so that the same data is not overwritten. It empties and refills.