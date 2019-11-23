
/*
 * 建表存储过程
 * mysql
 */
DELIMITER $$

CREATE  PROCEDURE `createZxDataTables`(in mark VARCHAR(255))
BEGIN 
        DECLARE `@i` INT(11);
        DECLARE `@l` INT(11);
        DECLARE `@createTableSql` VARCHAR(2560); 
        DECLARE `@createDataBaseSql` VARCHAR(2560); 
        DECLARE `@j` VARCHAR(10);
        SET `@l`=0;
        WHILE  `@l`< 10 DO   
        
        	SET @createDataBaseSql  = CONCAT('CREATE DATABASE  ',`mark`,'',`@l`,';');
            PREPARE stmt FROM @createDataBaseSql; 
            EXECUTE stmt; 
            
            SET `@i`=0; 
	        WHILE  `@i`< 100 DO   
	            IF `@i` < 10 THEN
	               SET `@j` = CONCAT(0,`@i`);
	            ELSE
	               SET `@j` = `@i`;
	            END IF;
	            -- 创建表        
	            SET @createTableSql = CONCAT('CREATE TABLE IF NOT EXISTS ',`mark`,'',`@l`,'.data',`@j`,'(
	                  `key` char(50) NOT NULL,
	                  `value` text NOT NULL,
	                  `version` int(11) NOT NULL DEFAULT 0,
	                  PRIMARY KEY (`key`)
	                )ENGINE=InnoDB DEFAULT CHARSET=utf8
					partition by key(`key`)
					partitions 100;'
	            ); 
	            PREPARE stmt FROM @createTableSql; 
	            EXECUTE stmt;                             
	            
				SET `@i`= `@i`+1; 
	        END WHILE;
	        
	        SET `@l`= `@l`+1;
        END WHILE;
END 

/**
 *  执行，等待时间较长
 *  mark可以自定义，程序访问时带上这个值即可
 */
CALL createZxDataTables("zxdata");