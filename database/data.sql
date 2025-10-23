
USE TrainStation;

--insert sample driver
INSERT INTO Users (username, password, email, name, userType) 
VALUES ('driver1', '$2a$10$fZEBlItGg93.9QTY2Dqs8Okt2pJ7rUX.//eq7J1xDFEoYJDHz.P1q', 'driver1@example.com', 'Driver One', 'DRIVER');
insert into Driver (userId,license,trainId) values (1,'ABC1020',1)


--insert sample operation manager
INSERT INTO Users (username, password, email, name, userType) 
VALUES ('opManager1', '$2a$10$fZEBlItGg93.9QTY2Dqs8Okt2pJ7rUX.//eq7J1xDFEoYJDHz.P1q', 'op@example.com', 'Operation Manager One', 'OPMANAGER');
insert into OpManager(userId,contactNumber) values (2,'0777233543')


--insert sample route and stops
insert into Route (routeName,durationMinutes,driverId,availableTime) values ('test route', 180, 1,'8:00 AM')
insert into Stops (routeId, stopName, stopOrder) values (1,'Kandy',1);
insert into Stops (routeId, stopName, stopOrder) values (1,'Matale',2);


--Insert sample train
insert into Train (trainName) values ('Train One');
insert into TrainRoute (trainId,routeId) values (1,1)
INSERT INTO TrainStatus (trainRouteId, stopId, status)
VALUES (1, 1, 'ON_TIME');


--Insert sample IT Officer
insert into Users (username,userType,password,email,name) values ('itOfficer1','ITOFFICER','$2a$10$fZEBlItGg93.9QTY2Dqs8Okt2pJ7rUX.//eq7J1xDFEoYJDHz.P1q','itofficer@123','IT Officer One') 
insert into ItOfficer (userId,accessLevel) values (3, 'Manager');



--Insert sample ticket officer
insert into Users (username,password,name,userType) values ('ticket1','$2a$10$fZEBlItGg93.9QTY2Dqs8Okt2pJ7rUX.//eq7J1xDFEoYJDHz.P1q','Ticket Officer One','TicketOfficer')
insert into TicketOfficer(userId,trainId) values (4,1)


--another sample driver
INSERT INTO Users (username, password, email, name, userType) 
VALUES ('driver2', '$2a$10$fZEBlItGg93.9QTY2Dqs8Okt2pJ7rUX.//eq7J1xDFEoYJDHz.P1q', 'driver2@example.com', 'Kamal Perera', 'DRIVER');
insert into Driver (userId,license,trainId) values (5,'BCW3212',2)


--insert sample customer service executive
INSERT INTO Users(username,password,userType, name, email) values ('service1','$2a$10$fZEBlItGg93.9QTY2Dqs8Okt2pJ7rUX.//eq7J1xDFEoYJDHz.P1q','CusService','Customer Service Executive 1','cusService@123')
INSERT INTO CustomerServiceExecutive values (6,'0111233456')

--insert sample passenger
INSERT INTO Users(username,password,userType, name, email) values ('passenger1','$2a$10$fZEBlItGg93.9QTY2Dqs8Okt2pJ7rUX.//eq7J1xDFEoYJDHz.P1q','passenger','Passenger1','passenger@123')
INSERT INTO Passenger(userId,passengerAddress) values (7,'Test Address')


