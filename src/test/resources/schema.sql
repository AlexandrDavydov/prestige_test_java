CREATE TABLE IF NOT EXISTS students (
    id INTEGER PRIMARY KEY AUTOINCREMENT,
    last_name TEXT NOT NULL,
    first_name TEXT NOT NULL,
    middle_name TEXT,
    contacts TEXT,
    birthday TEXT,
    lessons_count INTEGER DEFAULT 0,
    additional_info TEXT
);

CREATE TABLE IF NOT EXISTS coaches (
    id INTEGER PRIMARY KEY AUTOINCREMENT,
    last_name TEXT NOT NULL,
    first_name TEXT NOT NULL,
    middle_name TEXT,
    contacts TEXT,
    birthday TEXT,
    lessons_count INTEGER DEFAULT 0,
    lessons_paid INTEGER DEFAULT 0,
    student_payment REAL DEFAULT 0,
    additional_info TEXT
);

CREATE TABLE IF NOT EXISTS cards (
    id INTEGER PRIMARY KEY AUTOINCREMENT,
    name TEXT NOT NULL,
    price REAL NOT NULL,
    lessons_count INTEGER NOT NULL,
    duration TEXT,
    color TEXT,
    status TEXT,
    creation_date TEXT
);

CREATE TABLE IF NOT EXISTS lesson_templates (
    id INTEGER PRIMARY KEY AUTOINCREMENT,
    template_name TEXT NOT NULL,
    coach_id INTEGER NOT NULL,
    student_ids TEXT
);

CREATE TABLE IF NOT EXISTS lessons (
    id INTEGER PRIMARY KEY AUTOINCREMENT,
    lesson_name TEXT NOT NULL,
    date TEXT,
    coach_id INTEGER NOT NULL,
    student_ids TEXT,
    status TEXT
);
