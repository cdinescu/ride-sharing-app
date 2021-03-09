CREATE TABLE IF NOT EXISTS ride (
   id serial PRIMARY KEY,
   pickup_location VARCHAR(200),
   destination VARCHAR(200),
   ride_status VARCHAR(200)

);
