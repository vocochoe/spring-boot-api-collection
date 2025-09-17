-- 유저 데이터 (관리자, 의사, 환자)
INSERT INTO users (username, password, role) VALUES ('admin01', '1234', 'ADMIN');
INSERT INTO users (username, password, role) VALUES ('drkim01', 'abcd', 'DOCTOR');
INSERT INTO users (username, password, role) VALUES ('patient01', 'qwer', 'PATIENT');

-- 의사 프로필 (userId = 2와 연결)
INSERT INTO doctor (name, specialty, user_id) VALUES ('김철수', '내과', 2);

-- 예약 (doctorId = 1, patientId = 3)
INSERT INTO appointment (patient_name, appointment_time, doctor_id, user_id)
VALUES ('홍길동', '2025-09-19T10:00:00', 1, 3);

