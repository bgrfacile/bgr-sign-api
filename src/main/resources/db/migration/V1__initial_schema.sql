-------------------------------------------------------
-- Table des utilisateurs
-- Rôle : Stocker les informations de base pour l'authentification de tous les utilisateurs du système.
-- Utilisation : Gérer les identifiants (email, mot de passe) et les dates de création/mise à jour.
-------------------------------------------------------
CREATE TABLE users
(
    id            SERIAL PRIMARY KEY,
    email         VARCHAR(255) UNIQUE NOT NULL,
    password VARCHAR(255) NOT NULL,
    enabled BOOLEAN DEFAULT TRUE,
    created_at    TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at    TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-------------------------------------------------------
-- Table des rôles
-- Rôle : Définir les rôles (ex. administrateur, enseignant, étudiant, parent) disponibles dans le système.
-- Utilisation : Permettre l'attribution de droits spécifiques aux utilisateurs en fonction de leur rôle.
-------------------------------------------------------
CREATE TABLE roles
(
    id          SERIAL PRIMARY KEY,
    name        VARCHAR(50) UNIQUE NOT NULL,
    description TEXT
);

-------------------------------------------------------
-- Table des permissions
-- Rôle : Définir les différentes permissions pouvant être attribuées aux rôles.
-- Utilisation : Gérer des droits d'accès plus fins sur les fonctionnalités du système.
-------------------------------------------------------
CREATE TABLE permissions
(
    id          SERIAL PRIMARY KEY,
    name        VARCHAR(50) UNIQUE NOT NULL,
    description TEXT
);

-------------------------------------------------------
-- Table d'association entre rôles et permissions
-- Rôle : Associer plusieurs permissions à un rôle spécifique (relation N-N).
-- Utilisation : Gérer la configuration des droits d'accès pour chaque rôle.
-------------------------------------------------------
CREATE TABLE role_permissions
(
    role_id       INT NOT NULL REFERENCES roles (id) ON DELETE CASCADE,
    permission_id INT NOT NULL REFERENCES permissions (id) ON DELETE CASCADE,
    PRIMARY KEY (role_id, permission_id)
);

-------------------------------------------------------
-- Table d'association entre utilisateurs et rôles
-- Rôle : Attribuer un ou plusieurs rôles à chaque utilisateur (relation N-N).
-- Utilisation : Déterminer les droits et l'accès aux différentes fonctionnalités en fonction du rôle.
-------------------------------------------------------
CREATE TABLE user_roles
(
    user_id INT NOT NULL REFERENCES users (id) ON DELETE CASCADE,
    role_id INT NOT NULL REFERENCES roles (id) ON DELETE CASCADE,
    PRIMARY KEY (user_id, role_id)
);

-------------------------------------------------------
-- Table des classes
-- Rôle : Représenter les classes ou groupes d'étudiants dans l'établissement.
-- Utilisation : Identifier l'appartenance des étudiants à une classe et gérer l'année académique correspondante.
-------------------------------------------------------
CREATE TABLE classes
(
    id            SERIAL PRIMARY KEY,
    class_name    VARCHAR(50) UNIQUE NOT NULL,
    academic_year VARCHAR(9)
);

-------------------------------------------------------
-- Table des matières (subjects)
-- Rôle : Stocker les informations relatives aux matières enseignées.
-- Utilisation : Associer chaque cours à une matière avec une description pour plus de détails.
-------------------------------------------------------
CREATE TABLE subjects
(
    id           SERIAL PRIMARY KEY,
    subject_name VARCHAR(100) UNIQUE NOT NULL,
    description  TEXT
);

-------------------------------------------------------
-- Table des administrateurs
-- Rôle : Conserver les informations spécifiques aux utilisateurs ayant le rôle d'administrateur.
-- Utilisation : Gérer les détails complémentaires (prénom, nom, téléphone) des administrateurs.
-------------------------------------------------------
CREATE TABLE admins
(
    user_id      INT PRIMARY KEY REFERENCES users (id) ON DELETE CASCADE,
    first_name   VARCHAR(50),
    last_name    VARCHAR(50),
    phone_number VARCHAR(20)
);

-------------------------------------------------------
-- Table des enseignants
-- Rôle : Conserver les informations spécifiques aux enseignants.
-- Utilisation : Stocker le prénom, nom, date d'embauche et la spécialisation de l'enseignant.
-------------------------------------------------------
CREATE TABLE teachers
(
    user_id        INT PRIMARY KEY REFERENCES users (id) ON DELETE CASCADE,
    first_name     VARCHAR(50),
    last_name      VARCHAR(50),
    hire_date      DATE,
    specialization VARCHAR(100)
);

-------------------------------------------------------
-- Table des étudiants
-- Rôle : Conserver les informations spécifiques aux étudiants.
-- Utilisation : Stocker le prénom, nom, date de naissance et la référence à la classe à laquelle l'étudiant appartient.
-------------------------------------------------------
CREATE TABLE students
(
    user_id       INT PRIMARY KEY REFERENCES users (id) ON DELETE CASCADE,
    first_name    VARCHAR(50),
    last_name     VARCHAR(50),
    date_of_birth DATE,
    class_id      INT REFERENCES classes (id) ON DELETE SET NULL
);

-------------------------------------------------------
-- Table des parents/tuteurs
-- Rôle : Conserver les informations spécifiques aux parents ou tuteurs.
-- Utilisation : Gérer le prénom, nom et numéro de téléphone pour contacter le parent ou tuteur.
-------------------------------------------------------
CREATE TABLE parents
(
    user_id      INT PRIMARY KEY REFERENCES users (id) ON DELETE CASCADE,
    first_name   VARCHAR(50),
    last_name    VARCHAR(50),
    phone_number VARCHAR(20)
);

-------------------------------------------------------
-- Table de liaison Parent-Étudiant
-- Rôle : Gérer la relation entre les parents et les étudiants (relation N-N).
-- Utilisation : Enregistrer le type de relation (ex. parent, tuteur) entre un parent et un étudiant.
-------------------------------------------------------
CREATE TABLE parent_student
(
    parent_id         INT REFERENCES parents (user_id) ON DELETE CASCADE,
    student_id        INT REFERENCES students (user_id) ON DELETE CASCADE,
    relationship_type VARCHAR(20),
    PRIMARY KEY (parent_id, student_id)
);

-------------------------------------------------------
-- Table des cours
-- Rôle : Représenter les cours enseignés dans l'établissement.
-- Utilisation : Associer un enseignant, une matière et une classe à un horaire de cours spécifique.
-------------------------------------------------------
CREATE TABLE courses
(
    id         SERIAL PRIMARY KEY,
    teacher_id INT REFERENCES teachers (user_id) ON DELETE CASCADE,
    subject_id INT REFERENCES subjects (id) ON DELETE CASCADE,
    class_id   INT REFERENCES classes (id) ON DELETE CASCADE,
    schedule   TIMESTAMP,
    UNIQUE (teacher_id, subject_id, class_id, schedule)
);

-------------------------------------------------------
-- Table de présence
-- Rôle : Enregistrer la présence ou l'absence des étudiants lors des cours.
-- Utilisation : Stocker le statut de présence (present, absent, late, excused), la date du cours et l'enseignant qui a enregistré la présence.
-------------------------------------------------------
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

-------------------------------------------------------
-- Table de justification d'absence
-- Rôle : Gérer les demandes de justification d'absence soumises par les étudiants.
-- Utilisation : Stocker la raison, le chemin de la pièce jointe, l'état de la demande (pending, approved, rejected) ainsi que les informations sur qui a soumis et révisé.
-------------------------------------------------------
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

-------------------------------------------------------
-- Table des règles de présence
-- Rôle : Définir les règles applicables à la présence des étudiants.
-- Utilisation : Spécifier le nom de la règle, une description, le délai pour soumettre une justification, le pourcentage minimal de présence, et la date d'entrée en vigueur.
-------------------------------------------------------
CREATE TABLE attendance_rules
(
    id                          SERIAL PRIMARY KEY,
    rule_name                   VARCHAR(100) UNIQUE NOT NULL,
    description                 TEXT,
    justification_deadline_days INT NOT NULL,
    min_attendance_percentage   DECIMAL(5, 2),
    effective_date              DATE NOT NULL
);

-------------------------------------------------------
-- Table des notifications
-- Rôle : Enregistrer les notifications envoyées aux utilisateurs.
-- Utilisation : Stocker le titre, le message, le lien vers une entité (si nécessaire), l'état de lecture et la date de création de la notification.
-------------------------------------------------------
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
