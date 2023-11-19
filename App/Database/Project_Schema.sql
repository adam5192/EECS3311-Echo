-- Diagram/table constructions
create schema Project_Database;
drop schema Project_Database;
use Project_Database;

create table FoodName (
 FoodID INT(11) NOT NULL,
 FoodCode INT(8) NOT NULL,
 FoodGroupID INT(15),
 FoodSourceID INT(15),
 FoodDescription VARCHAR(255),
 FoodDescriptionF VARCHAR(255),
 CountryCode INT(20),
 FoodDateOfEntry DATETIME,
 FoodDateOfPublication DATETIME,
 ScientificName VARCHAR(100),
 constraint FoodName_PK PRIMARY KEY (FoodID),
 constraint FoodGroup_FK FOREIGN KEY (FoodGroupID) references FoodGroup(FoodGroupID),
 constraint FoodSource_FK FOREIGN KEY (FoodSourceID) references FoodSource(FoodSourceID));

create table NutrientAmount (
 FoodID INT(8) NOT NULL,
 NutrientNameID INT(4) NOT NULL,
 NutrientValue INT(12),
 StandardError FLOAT(8, 4),
 NumberOfObservations INT(6),
 NutrientSourceID INT(15) NOT NULL,
 NutrientDateEntry DATETIME,
 constraint NutrientAmount_PK PRIMARY KEY (FoodID, NutrientNameID, NutrientSourceID),
 constraint FoodName_FK FOREIGN KEY (FoodID) references FoodName(FoodID),
 constraint NutrientNameID FOREIGN KEY (NutrientNameID) references NutrientName(NutrientNameID),
 constraint NutrientSourceID FOREIGN KEY (NutrientSourceID) references NutrientSource(NutrientSourceID));

create table ConversionFactor (
 FoodID INT(8) NOT NULL,
 MeasureID INT(10) NOT NULL,
 ConversionFactorValue INT(10),
 ConvFactorDateOfEntry DATETIME,
 constraint ConvFactor_PK PRIMARY KEY (FoodID, MeasureID),
 constraint ConvFoodName_FK FOREIGN KEY (FoodID) references FoodName(FoodID),
 constraint MeasureName_FK FOREIGN KEY (MeasureID) references MeasureName(MeasureID));

create table RefuseAmount (
 FoodID INT(8) NOT NULL,
 RefuseID INT(10) NOT NULL,
 RefuseAmount FLOAT(9, 5),
 RefuseDateOfEntry DATETIME,
 constraint RefuseAmount_PK PRIMARY KEY (FoodID, RefuseID),
 constraint RefuseFoodName_FK FOREIGN KEY (FoodID) references FoodName(FoodID),
 constraint RefuseName_FK FOREIGN KEY (RefuseID) references RefuseName(RefuseID));

create table YieldAmount (
 FoodID INT(8) NOT NULL,
 YieldID INT(10) NOT NULL,
 YieldAmount FLOAT(9, 5),
 YieldDateOfEntry DATETIME,
 constraint YieldAmount_PK PRIMARY KEY (FoodID, YieldID),
 constraint YieldFoodName_FK FOREIGN KEY (FoodID) references FoodName(FoodID),
 constraint YieldName_FK FOREIGN KEY (YieldID) references YieldName(YieldID));

create table FoodGroup (
 FoodGroupID INT(15) NOT NULL,
 FoodGroupCode INT(15) NOT NULL,
 FoodGroupName VARCHAR(200),
 FoodGroupNameF VARCHAR(200),
 constraint FoodGroup_PK PRIMARY KEY (FoodGroupID));

create table FoodSource (
 FoodSourceID INT(15) NOT NULL,
 FoodSourceCode INT(15) NOT NULL,
 FoodSourceDescription VARCHAR(200),
 FoodSourceDescriptionF VARCHAR(200),
 constraint FoodSource_PK PRIMARY KEY (FoodSourceID));

create table NutrientName (
 NutrientNameID INT(4) NOT NULL,
 NutrientCode INT(15) NOT NULL,
 NutrientSymbol VARCHAR(10),
 Unit VARCHAR(8),
 NutrientName VARCHAR(200),
 NutrientNameF VARCHAR(200),
 TagName VARCHAR(20),
 NutrientDecimals INT(15),
 constraint NutrientName_PK PRIMARY KEY (NutrientNameID));

create table NutrientSource (
 NutrientSourceID INT(15) NOT NULL,
 NutrientSourceCode INT(15),
 NutrientSourceDescription VARCHAR(200),
 NutrientSourceDescriptionF VARCHAR(200),
 constraint NutrientSource_PK PRIMARY KEY (NutrientSourceID));

create table MeasureName (
 MeasureID INT(10) NOT NULL,
 MeasureName VARCHAR(200),
 MeasureNameF VARCHAR(200),
 constraint MeasureName_PK PRIMARY KEY (MeasureID));
 
create table RefuseName (
 RefuseID INT(10) NOT NULL,
 RefuseName VARCHAR(200),
 RefuseNameF VARCHAR(200),
 constraint RefuseName_PK PRIMARY KEY (RefuseID));

create table YieldName (
 YieldID INT(10) NOT NULL,
 YieldName VARCHAR(200),
 YieldNameF VARCHAR(200),
 constraint YieldName_PK PRIMARY KEY (YieldID));

create table UserProfile (
 UserID INT(8) NOT NULL,
 Sex BOOLEAN,
 Birth DATETIME,
 CurrHeight FLOAT(9, 2),
 CurrWeight FLOAT(9, 2),
 FatLvl INT(4),
 IsMetric BOOLEAN,
 BMRSetting INT(1),
 constraint User_PK PRIMARY KEY (UserID));

-- Log type: 0 for none, 1 for DataLog, 2 for MealLog, 3 for ExerciseLog
create table ProfileLog (
 LogDate DATE NOT NULL,
 LogType INT(1) NOT NULL,
 UserID INT(8) NOT NULL,
 constraint Log_PK PRIMARY KEY (LogDate, LogType, UserID),
 constraint User_FK FOREIGN KEY (UserID) references UserProfile(UserID));

 create table DataLog (
  LogDate DATE NOT NULL,
  LogType INT(1) NOT NULL,
  UserID INT(8) NOT NULL,
  LogWeight FLOAT(9, 2),
  LogHeight FLOAT(9, 2),
  constraint DataLog_PK PRIMARY KEY (LogDate, UserID),
  constraint DataLog_FK FOREIGN KEY (LogDate, LogType, UserID) references ProfileLog(LogDate, LogType, UserID));

create table MealLog (
 LogDate DATE NOT NULL,
 LogType INT(1) NOT NULL,
 UserID INT (8) NOT NULL,
 MealType VARCHAR(200) NOT NULL,
 MealName VARCHAR(200) NOT NULL,
 CaloVal INT(10),
 CarbVal INT(10),
 FatVal INT(10),
 ProtVal INT(10),
 OthersVal INT(10),
 Serving INT(3), -- Number of servings of the ingredient in the meal
 constraint MealLog_PK PRIMARY KEY (LogDate, UserID, MealName),
 constraint MealLog_Log_FK FOREIGN KEY (LogDate, LogType, UserID) references ProfileLog(LogDate, LogType, UserID));

 create table ExerciseLog (
  LogDate DATE NOT NULL,
  LogType INT(1) NOT NULL,
  UserID INT(8) NOT NULL,
  CaloBurnt INT(8),
  ExerciseTime FLOAT(8, 2),
  ExerciseName VARCHAR(200),
  constraint ExerciseLog_PK PRIMARY KEY (LogDate, UserID),
  constraint ExerciseLog_FK FOREIGN KEY (LogDate, LogType, UserID) references ProfileLog(LogDate, LogType, UserID));
  
-- Triggers
-- Drop tables
drop table FoodName;
drop table NutrientAmount;
drop table NutrientName;
drop table FoodGroup;
drop table FoodSource;
drop table NutrientSource;

-- Less useful tables
drop table ConversionFactor;
drop table RefuseAmount;
drop table YieldAmount;
drop table MeasureName;
drop table RefuseName;
drop table YieldName;

drop table UserProfile;
drop table ProfileLog;
drop table DataLog;
drop table MealLog;
drop table ExerciseLog;