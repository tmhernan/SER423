DROP TABLE place;

CREATE TABLE place (
	address_title VARCHAR(255),
    address_street VARCHAR(255),
    elevation DOUBLE(100,1),
    latitude DOUBLE(100,6),
    longitude DOUBLE(100,6),
    place_name VARCHAR(255) PRIMARY KEY,
    image TEXT,
    place_decription TEXT,
    place_category VARCHAR(255)		
);

INSERT INTO place VALUES
("ASU West Campus",
"13591 N 47th Ave$Phoenix AZ 85051",
1100.0, 33.608979, -112.159469,
"ASU-West", "asuwest", "Home of ASU's Applied Computing Program","School"
);

INSERT INTO place VALUES
("University of Alaska at Anchorage",
"290 Spirit Dr$Anchorage AK 99508",
0.0, 61.189748, -149.826721,
"UAK-Anchorage", "univalaska", "University of Alaska's largest campus","School"
);

INSERT INTO place VALUES
("University of San Diego",
"5998 Alcala Park$San Diego CA 92110",
200.0, 32.771923, -117.188204,
"Toreros", "univsandiego", "The University of San Diego, a private Catholic undergraduate university.","School"
);

INSERT INTO place VALUES
("Will Rogers Airport",
"1725 Ahkovak St$Barrow AK 99723",
5.0, 71.287881, -156.779417,
"Barrow-Alaska", "barrowalaska", "The only real way in and out of Barrow Alaska.",
"Travel"
);

INSERT INTO place VALUES
("Calgary International Airport",
"2000 Airport Rd NE$Calgary AB T2E 6Z8 Canada",
3556.0, 51.131377, 51.131377,
"Calgary-Alberta", "calgaryalberta","The home of the Calgary Stampede Celebration",
"Travel"
);

INSERT INTO place VALUES
(   "Renaissance London Heathrow Airport",
    "5 Mondial Way$Harlington Hayes UB3 UK",
	82.0,
	51.481959,
	-0.445286,
    "London-England",
    "londonunderground",
    "Renaissance Hotel at the Heathrow Airport",
    "Travel"
);

INSERT INTO place VALUES
(   "Courtyard Moscow City Center",
    "Voznesenskiy per 7 $ Moskva Russia 125009",
    512.0,
    55.758666,
    37.604058,
    "Moscow-Russia",
    "moscow",
    "The Marriott Courtyard in downtown Moscow",
    "Travel"
);

INSERT INTO place VALUES
(   "New York City Hall",
    "1 Elk St$New York NY 10007",
    2.0,
    40.712991,
    -74.005948,
    "New-York-NY",
    "newyork",
    "New York City Hall at West end of Brooklyn Bridge",
    "Travel"
);

INSERT INTO place VALUES
(   "Cathedral Notre Dame de Paris",
    "6 Parvis Notre-Dame Pl Jean-Paul-II$75004 Paris France",
    115.0,
    48.852972,
    2.349910,
    "Notre-Dame-Paris",
    "notredame3rdstatuewest",
    "The 13th century cathedral with gargoyles, one of the first flying buttress, and home of the purported crown of thorns.",
    "Travel"
);

INSERT INTO place VALUES
(   null,
	null,
    6000.0,
    33.477524,
    -111.134345,
    "Circlestone",
    "circlestone",
    "Indian Ruins located on the second highest peak in the Superstition Wilderness of the Tonto National Forest. Leave Fireline at Turney Spring (33.487610,-111.132400)",
    "Hike"
);

INSERT INTO place VALUES
(   null,
	null,
    5000.0,
    33.491154,
    -111.155385,
    "Reavis-Ranch",
    "reavisranch",
    "Historic Ranch in Superstition Mountains famous for Apple orchards",
    "Hike"
);

INSERT INTO place VALUES
(   null,
	null,
    4500.0,
    33.422212,
    -111.173393,
    "Rogers-Trailhead",
    "rogerstrough",
    "Trailhead for hiking to Rogers Canyon Ruins and Reavis Ranch",
    "Hike"
);

INSERT INTO place VALUES
(   null,
	null,
    3900.0,
    33.441499,
    -111.182511,
    "Reavis-Grave",
    "reavisgrave",
    "Grave site of Reavis Ranch Proprietor.",
    "Hike"
);

INSERT INTO place VALUES
(   "Muir Woods National Monument",
    "1 Muir Woods Rd$Mill Valley CA 94941",
    350.0,
    37.8912,
    -122.5957,
    "Muir-Woods",
    "muirwoods",
    "Redwood forest North of San Francisco, surrounded by Mount Tamalpais State Park.",
    "Hike"
);

INSERT INTO place VALUES
(   "Usery Mountain Recreation Area",
    "3939 N Usery Pass Rd$Mesa AZ 85207",
    3130.0,
    33.476069,
    -111.595709,
    "Windcave-Peak",
    "windcavepeak",
    "Beyond the Wind Cave is a half mile trail with 250' additional elevation to the peak overlooking Usery and the Valley.",
    "Hike"
);
