create database TrainStation;
use TrainStation;


-- Create Users Table
CREATE TABLE Users (
    userId INT PRIMARY KEY IDENTITY(1,1),
    username VARCHAR(50) NOT NULL UNIQUE,
    password VARCHAR(100) NOT NULL,
    email VARCHAR(100),
    name VARCHAR(100),
    userType VARCHAR(50) NOT NULL
);


-- Create Driver Table
CREATE TABLE Driver (
    userId INT PRIMARY KEY,
    license VARCHAR(20) NOT NULL,
    trainId int,
    CONSTRAINT fk_Driver FOREIGN KEY (userId) 
        REFERENCES Users(userId)
        ON DELETE CASCADE
        ON UPDATE CASCADE
);



-- Create Operations Manager Table
CREATE TABLE OpManager (
    userId INT PRIMARY KEY,
    contactNumber VARCHAR(20),
    CONSTRAINT fk_OpManager FOREIGN KEY (userId) 
        REFERENCES Users(userId)
        ON DELETE CASCADE
        ON UPDATE CASCADE
);


CREATE TABLE CustomerServiceExecutive(
    userId INT PRIMARY KEY,
    contactNum INT,

    constraint fk_CustomerServiceExecutive foreign key(userId) references Users(userId)


)
-- Create Messages Table
CREATE TABLE Msgs (
    msgId INT PRIMARY KEY IDENTITY(1,1),
    senderId INT NULL,
    receiverId INT NULL,
    msgText VARCHAR(500) NOT NULL,
    msgTime DATETIME DEFAULT GETDATE(),
    CONSTRAINT fk_Msgs_Sender FOREIGN KEY (senderId) REFERENCES Users(userId),
    CONSTRAINT fk_Msgs_Receiver FOREIGN KEY (receiverId) REFERENCES Users(userId)
);

-- Create Route Table
CREATE TABLE Route (
    routeId INT PRIMARY KEY IDENTITY(1,1),
    routeName VARCHAR(100) NOT NULL UNIQUE,
    durationMinutes INT,
    availableTime varchar(20),
    driverId INT NULL,
    CONSTRAINT fk_Route_Driver FOREIGN KEY (driverId)
        REFERENCES Driver(userId)
        ON DELETE SET NULL
        ON UPDATE CASCADE
);

-- Create Stops Table (for each route)
CREATE TABLE Stops (
    stopId INT PRIMARY KEY IDENTITY(1,1),
    routeId INT NOT NULL,
    stopName VARCHAR(100) NOT NULL,
    stopOrder INT NOT NULL,
    CONSTRAINT fk_Stops_Route FOREIGN KEY (routeId)
        REFERENCES Route(routeId)
        ON DELETE CASCADE
        ON UPDATE CASCADE
);

-- Create Train Table
CREATE TABLE Train (
    trainId INT PRIMARY KEY IDENTITY(1,1),
    trainName VARCHAR(100) UNIQUE NOT NULL
    

);




-- Create TrainSeats Table
CREATE TABLE TrainSeats (
    trainId INT PRIMARY KEY,
    seatCount INT NOT NULL,
    CONSTRAINT fk_TrainSeats FOREIGN KEY (trainId)
        REFERENCES Train(trainId)
        ON DELETE CASCADE
        ON UPDATE CASCADE
);

CREATE TABLE TrainRoute (
    trainRouteId INT PRIMARY KEY IDENTITY(1,1),
    trainId INT NOT NULL,
    routeId INT NOT NULL,
    active BIT DEFAULT 1,
    FOREIGN KEY (trainId) REFERENCES Train(trainId) ON DELETE CASCADE,

    FOREIGN KEY (routeId) REFERENCES Route(routeId) ON DELETE CASCADE
);

CREATE TABLE TrainStatus (
    statusId INT PRIMARY KEY IDENTITY(1,1),
    trainRouteId INT NOT NULL,
    stopId INT NULL,
    stopName VARCHAR(70) ,
    status VARCHAR(20) DEFAULT 'CANCELLED',
    timestamp DATETIME DEFAULT GETDATE(),
    FOREIGN KEY (trainRouteId) REFERENCES TrainRoute(trainRouteId) ON DELETE CASCADE,
    FOREIGN KEY (stopId) REFERENCES Stops(stopId) ON UPDATE CASCADE ON DELETE NO ACTION,

    
);

create table ItOfficer(
    userId int NOT NULL,
    accessLevel varchar(30)

    constraint pk_ItOfficer primary key(userId),
    constraint fk_ItOfficer foreign key(userId) references Users(userId)
        ON DELETE CASCADE
        ON UPDATE CASCADE,

)


create table Passenger(
    userId int,
    passengerAddress varchar(100),

    constraint pk_passenger primary key(userId),
    constraint fk_passenger foreign key(userId) references Users(userId)

)
Create table Booking(
    bookingId int primary key identity(1,1),
    bookingDate DATETIME,
    bookingStatus varchar(30) DEFAULT 'Pending',
    class varchar(30),
    passengerId int,
    trainId int,
    numSeats INT DEFAULT 1,
    seat varchar(30),


    constraint fk_booking_passengerId foreign key(passengerId) references Passenger(userId)
        ON DELETE CASCADE
        ON UPDATE CASCADE,
    constraint fk_booking_trainId foreign key(trainId) references Train(trainId)
        ON DELETE CASCADE
        ON UPDATE CASCADE

);



Create table Payment(
    paymentId int primary key identity(1,1),
    bookingId int,
    amount money,
    paymentdate datetime,

    constraint fk_payment foreign key(bookingId) references Booking(bookingId)
         ON DELETE CASCADE
         ON UPDATE CASCADE

)

CREATE TABLE BookingSeat (
    bookingSeatId INT PRIMARY KEY IDENTITY(1,1),
    bookingId INT NOT NULL,
    seatNumber VARCHAR(10) NOT NULL,
    FOREIGN KEY (bookingId) REFERENCES Booking(bookingId) ON DELETE CASCADE
);

create table TicketOfficer(
    userId int,
    trainId int,

    constraint pk_ticketOfficer primary key(userId),
    constraint fk_ticketOfficer foreign key(userId) references Users(userId)
        ON DELETE CASCADE
        ON UPDATE CASCADE,
    constraint fk_ticketOfficer_Train foreign key(trainId) references Train(trainId)
        ON DELETE CASCADE
        ON UPDATE CASCADE
)

CREATE TABLE ViolationReport(
    reportId int primary key identity(1,1),
    ticketOfficerId int,
    reportDate datetime,
    passengerId int,
    trainId int,
    violationType varchar(50),

    constraint fk_violationReport foreign key(ticketOfficerId) references TicketOfficer(userId)
         ON DELETE CASCADE,
    constraint fk_violationReport_passenger foreign key(passengerId) references Passenger(userId),

    constraint fk_violationReport_train foreign key(trainId) references Train(trainId) ON DELETE NO ACTION
       

)

CREATE TABLE Concern(
    concernId int primary key identity(1,1),
    passengerId int,
    date_submitted datetime,
    status varchar(20),
    description varchar(100),

    constraint fk_concern foreign key(passengerId) references Passenger(userId)
        ON DELETE CASCADE
        ON UPDATE CASCADE
)

CREATE TABLE Reply(
    replyId int primary key identity(1,1),
    concernId int,
    execId int,
    replyText varchar(100),
    replyTime datetime,

    constraint fk_reply foreign key(concernId) references Concern(concernId)
        ON DELETE CASCADE
        ON UPDATE CASCADE,
    constraint fk_reply2 foreign key(execId) references CustomerServiceExecutive(userId)
        ON DELETE NO ACTION
    
)




GO
-- Trigger: assign seat(s) when Booking rows are inserted.
-- Assumes Booking has columns: bookingId, trainId, class, bookingStatus, seat (nullable)
-- Assumes Ticket table exists (see DDL above).
-- Seat prefixes: First -> 'A' (75 seats), Second -> 'B' (100 seats), Third -> 'C' (150 seats)


CREATE TRIGGER trg_AssignSeatsAfterPayment
ON Payment
AFTER INSERT
AS
BEGIN
    SET NOCOUNT ON;

    DECLARE @bookingId INT, @trainId INT, @class VARCHAR(20), @numSeats INT;
    DECLARE @prefix CHAR(1), @maxSeats INT, @currentSeats INT;
    DECLARE @i INT;

    -- Loop through each new payment
    DECLARE payment_cursor CURSOR FOR
        SELECT bookingId FROM inserted;

    OPEN payment_cursor;
    FETCH NEXT FROM payment_cursor INTO @bookingId;

    WHILE @@FETCH_STATUS = 0
    BEGIN
        -- Get booking info
        SELECT @trainId = trainId, @class = class, @numSeats = numSeats
        FROM Booking
        WHERE bookingId = @bookingId;

        -- Determine class prefix and capacity
        IF @class = 'First'
        BEGIN
            SET @prefix = 'A';
            SET @maxSeats = 75;
        END
        ELSE IF @class = 'Second'
        BEGIN
            SET @prefix = 'B';
            SET @maxSeats = 100;
        END
        ELSE IF @class = 'Third'
        BEGIN
            SET @prefix = 'C';
            SET @maxSeats = 150;
        END

        -- Count current seats already assigned for this train & class
        SELECT @currentSeats = COUNT(*)
        FROM BookingSeat bs
        JOIN Booking b ON bs.bookingId = b.bookingId
        WHERE b.trainId = @trainId AND b.class = @class;

        -- Check if enough seats available
        IF (@currentSeats + @numSeats) > @maxSeats
        BEGIN
            RAISERROR('Not enough seats available in this class for this train.', 16, 1);
            ROLLBACK TRANSACTION;
            RETURN;
        END

        -- Assign seats
        SET @i = 1;
        WHILE @i <= @numSeats
        BEGIN
            INSERT INTO BookingSeat (bookingId, seatNumber)
            VALUES (@bookingId, CONCAT(@prefix, @currentSeats + @i));

            SET @i = @i + 1;
        END

        -- Update booking status
        UPDATE Booking
        SET bookingStatus = 'Confirmed'
        WHERE bookingId = @bookingId;

        FETCH NEXT FROM payment_cursor INTO @bookingId;
    END

    CLOSE payment_cursor;
    DEALLOCATE payment_cursor;
END;
GO

GO








