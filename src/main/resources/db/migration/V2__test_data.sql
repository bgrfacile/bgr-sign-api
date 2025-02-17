-- 1. Insérer des rôles
INSERT INTO roles (name, description)
VALUES ('admin', 'Administrateur de l''application'),
       ('teacher', 'Enseignant'),
       ('student', 'Étudiant'),
       ('parent', 'Parent ou tuteur');

-- 2. Insérer des permissions
INSERT INTO permissions (name, description)
VALUES ('create_course', 'Créer des cours'),
       ('edit_course', 'Modifier des cours'),
       ('view_attendance', 'Voir la présence'),
       ('approve_justification', 'Approuver les justifications d''absence');

-- 3. Associer rôles et permissions
-- On suppose ici que les rôles et permissions ont été insérés dans l'ordre :
-- roles : admin=1, teacher=2, student=3, parent=4
-- permissions : create_course=1, edit_course=2, view_attendance=3, approve_justification=4

-- Pour l'administrateur : toutes les permissions
INSERT INTO role_permissions (role_id, permission_id)
VALUES (1, 1),
       (1, 2),
       (1, 3),
       (1, 4),
-- Pour l'enseignant : création, édition et visualisation de présence
       (2, 1),
       (2, 2),
       (2, 3),
-- Pour l'étudiant : uniquement la visualisation de présence
       (3, 3),
-- Pour le parent : également la visualisation de présence
       (4, 3);

-- 4. Insérer des utilisateurs
INSERT INTO users (email, password)
VALUES ('admin@example.com', '$2a$10$rmzMv49gdIDI0EaPyGgLCucj1bmrpbFHzdgvSgMa0GTnInAwyHQQ2'),    -- password
       ('teacher1@example.com', '$2a$10$rmzMv49gdIDI0EaPyGgLCucj1bmrpbFHzdgvSgMa0GTnInAwyHQQ2'), -- password
       ('student1@example.com', '$2a$10$rmzMv49gdIDI0EaPyGgLCucj1bmrpbFHzdgvSgMa0GTnInAwyHQQ2'), -- password
       ('parent1@example.com', '$2a$10$rmzMv49gdIDI0EaPyGgLCucj1bmrpbFHzdgvSgMa0GTnInAwyHQQ2');
-- password

-- 5. Associer les utilisateurs à leurs rôles
-- On suppose ici que les utilisateurs ont été insérés dans l'ordre :
-- admin=1, teacher=2, student=3, parent=4
INSERT INTO user_roles (user_id, role_id)
VALUES (1, 1), -- admin
       (2, 2), -- enseignant
       (3, 3), -- étudiant
       (4, 4); -- parent

-- 6. Remplir les tables spécifiques

-- Table des administrateurs
INSERT INTO admins (user_id, first_name, last_name, phone_number)
VALUES (1, 'Alice', 'Admin', '0102030405');

-- Table des enseignants
INSERT INTO teachers (user_id, first_name, last_name, hire_date, specialization)
VALUES (2, 'Bob', 'Teacher', '2020-09-01', 'Mathématiques');

-- Table des classes
INSERT INTO classes (class_name, academic_year)
VALUES ('Classe A', '2024-2025'),
       ('Classe B', '2024-2025');

-- Table des étudiants (on associe l'étudiant à "Classe A", dont l'id sera 1)
INSERT INTO students (user_id, first_name, last_name, date_of_birth, class_id)
VALUES (3, 'Charlie', 'Student', '2005-04-15', 1);

-- Table des parents
INSERT INTO parents (user_id, first_name, last_name, phone_number)
VALUES (4, 'Diane', 'Parent', '0607080910');

-- Table de liaison Parent-Étudiant (on relie le parent à l'étudiant)
INSERT INTO parent_student (parent_id, student_id, relationship_type)
VALUES (4, 3, 'parent');

-- 7. Insérer des matières
INSERT INTO subjects (subject_name, description)
VALUES ('Mathématiques', 'Étude des nombres et des formes'),
       ('Histoire', 'Étude des événements passés'),
       ('Sciences', 'Étude du monde naturel');

-- 8. Insérer des cours
-- Exemple : cours de Mathématiques pour la "Classe A" (id=1) donné par l'enseignant (user_id=2)
INSERT INTO courses (teacher_id, subject_id, class_id, schedule)
VALUES (2, 1, 1, '2024-09-01 09:00:00'),
       -- Un autre cours : Sciences pour la "Classe B" (id=2)
       (2, 3, 2, '2024-09-01 11:00:00');

-- 9. Enregistrer des présences pour l'étudiant
INSERT INTO attendances (student_id, course_id, date, status, recorded_by)
VALUES (3, 1, '2024-09-01', 'present', 2),
       (3, 1, '2024-09-02', 'absent', 2);

-- 10. Justification d'absence pour la seconde présence (on suppose que l'id de l'absence est 2)
INSERT INTO absence_justifications (attendance_id, reason, attachment_path, submitted_by, status, reviewed_by)
VALUES (2, 'Maladie', '/path/to/doctor_note.pdf', 3, 'pending', 1);

-- 11. Insérer une règle de présence
INSERT INTO attendance_rules (rule_name, description, justification_deadline_days, min_attendance_percentage,
                              effective_date)
VALUES ('Règle Standard', 'Exigence minimale de présence', 3, 75.00, '2024-09-01');

-- 12. Insérer des notifications
INSERT INTO notifications (user_id, title, message, related_entity_type, related_entity_id, is_read)
VALUES (3, 'Alerte de présence', 'Votre présence dans le cours est en dessous du seuil requis.', 'course', 1, false),
       (4, 'Nouvelle notification', 'Une nouvelle justification d''absence a été soumise.', 'absence_justification', 1,
        false);
