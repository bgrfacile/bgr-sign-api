-- 1. Table des utilisateurs (authentification de base)
CREATE TABLE users
(
    id            SERIAL PRIMARY KEY,
    email         VARCHAR(255) UNIQUE NOT NULL,
    password_hash VARCHAR(255)        NOT NULL,
    created_at    TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at    TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- 2. Tables pour le système de rôles et permissions

-- Table des rôles
CREATE TABLE roles
(
    id          SERIAL PRIMARY KEY,
    name        VARCHAR(50) UNIQUE NOT NULL,
    description TEXT
);

-- Table des permissions
CREATE TABLE permissions
(
    id          SERIAL PRIMARY KEY,
    name        VARCHAR(50) UNIQUE NOT NULL,
    description TEXT
);

-- Association entre rôles et permissions (relation N-N)
CREATE TABLE role_permissions
(
    role_id       INT NOT NULL REFERENCES roles (id) ON DELETE CASCADE,
    permission_id INT NOT NULL REFERENCES permissions (id) ON DELETE CASCADE,
    PRIMARY KEY (role_id, permission_id)
);

-- Association entre utilisateurs et rôles (relation N-N)
CREATE TABLE user_roles
(
    user_id INT NOT NULL REFERENCES users (id) ON DELETE CASCADE,
    role_id INT NOT NULL REFERENCES roles (id) ON DELETE CASCADE,
    PRIMARY KEY (user_id, role_id)
);

-- 3. Autres entités du domaine

-- Table classe
CREATE TABLE classes
(
    id            SERIAL PRIMARY KEY,
    class_name    VARCHAR(50) UNIQUE NOT NULL,
    academic_year VARCHAR(9)
);

-- Table matière
CREATE TABLE subjects
(
    id           SERIAL PRIMARY KEY,
    subject_name VARCHAR(100) UNIQUE NOT NULL,
    description  TEXT
);

-- 4. Tables spécifiques aux différents types d'utilisateurs

-- Table administrateur
CREATE TABLE admins
(
    user_id      INT PRIMARY KEY REFERENCES users (id) ON DELETE CASCADE,
    first_name   VARCHAR(50),
    last_name    VARCHAR(50),
    phone_number VARCHAR(20)
);

-- Table enseignant
CREATE TABLE teachers
(
    user_id        INT PRIMARY KEY REFERENCES users (id) ON DELETE CASCADE,
    first_name     VARCHAR(50),
    last_name      VARCHAR(50),
    hire_date      DATE,
    specialization VARCHAR(100)
);

-- Table étudiant
CREATE TABLE students
(
    user_id       INT PRIMARY KEY REFERENCES users (id) ON DELETE CASCADE,
    first_name    VARCHAR(50),
    last_name     VARCHAR(50),
    date_of_birth DATE,
    class_id      INT REFERENCES classes (id) ON DELETE SET NULL
);

-- Table parent/tuteur
CREATE TABLE parents
(
    user_id      INT PRIMARY KEY REFERENCES users (id) ON DELETE CASCADE,
    first_name   VARCHAR(50),
    last_name    VARCHAR(50),
    phone_number VARCHAR(20)
);

-- Table de liaison Parent-Étudiant
CREATE TABLE parent_student
(
    parent_id         INT REFERENCES parents (user_id) ON DELETE CASCADE,
    student_id        INT REFERENCES students (user_id) ON DELETE CASCADE,
    relationship_type VARCHAR(20),
    PRIMARY KEY (parent_id, student_id)
);

-- 5. Autres entités du domaine (cours, présence, notifications, etc.)

-- Table cours (association Enseignant-Matière-Classe)
CREATE TABLE courses
(
    id         SERIAL PRIMARY KEY,
    teacher_id INT REFERENCES teachers (user_id) ON DELETE CASCADE,
    subject_id INT REFERENCES subjects (id) ON DELETE CASCADE,
    class_id   INT REFERENCES classes (id) ON DELETE CASCADE,
    schedule   TIMESTAMP,
    UNIQUE (teacher_id, subject_id, class_id)
);

-- Table présence
CREATE TABLE attendances
(
    id          SERIAL PRIMARY KEY,
    student_id  INT REFERENCES students (user_id) ON DELETE CASCADE,
    course_id   INT REFERENCES courses (id) ON DELETE CASCADE,
    date        DATE NOT NULL,
    status      VARCHAR(20) CHECK (status IN ('present', 'absent', 'late', 'excused')),
    recorded_by INT REFERENCES teachers (user_id) ON DELETE SET NULL,
    recorded_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- Table justification d'absence
CREATE TABLE absence_justifications
(
    id              SERIAL PRIMARY KEY,
    attendance_id   INT REFERENCES attendances (id) ON DELETE CASCADE,
    reason          TEXT NOT NULL,
    attachment_path VARCHAR(255),
    submitted_by    INT REFERENCES users (id) ON DELETE SET NULL,
    status          VARCHAR(20) CHECK (status IN ('pending', 'approved', 'rejected')) DEFAULT 'pending',
    reviewed_by     INT REFERENCES users (id) ON DELETE SET NULL,
    created_at      TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at      TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- Table des règles de présence
CREATE TABLE attendance_rules
(
    id                          SERIAL PRIMARY KEY,
    rule_name                   VARCHAR(100) UNIQUE NOT NULL,
    description                 TEXT,
    justification_deadline_days INT NOT NULL,
    min_attendance_percentage   DECIMAL(5, 2),
    effective_date              DATE NOT NULL
);

-- Table notifications
CREATE TABLE notifications
(
    id                  SERIAL PRIMARY KEY,
    user_id             INT REFERENCES users (id) ON DELETE CASCADE,
    title               VARCHAR(255) NOT NULL,
    message             TEXT NOT NULL,
    related_entity_type VARCHAR(50),
    related_entity_id   INT,
    is_read             BOOLEAN DEFAULT false,
    created_at          TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);
