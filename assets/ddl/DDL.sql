-- 데이터베이스가 없으면 생성
CREATE DATABASE IF NOT EXISTS `nbti`
    DEFAULT CHARACTER SET utf8mb4
    COLLATE utf8mb4_general_ci;

-- 사용할 데이터베이스 선택
USE `nbti`;

-- 외래 키 제약 조건 무시
SET FOREIGN_KEY_CHECKS = 0;

-- 1. 기초 테이블
DROP TABLE IF EXISTS `answer_type`;
DROP TABLE IF EXISTS `category`;

-- 2. 사용자 테이블
DROP TABLE IF EXISTS `user`;
DROP TABLE IF EXISTS `point_history`;

-- 3. 검사 결과 테이블
DROP TABLE IF EXISTS `test_result`;
DROP TABLE IF EXISTS `test_problem`;

-- 4. 문제/학습 테이블
DROP TABLE IF EXISTS `problem`;
DROP TABLE IF EXISTS `study`;
DROP TABLE IF EXISTS `study_result`;

-- 5. 이의 제기 테이블
DROP TABLE IF EXISTS `objection`;

-- 외래 키 제약 조건 다시 활성화
SET FOREIGN_KEY_CHECKS = 1;					


-- 답안 유형 테이블 생성
CREATE TABLE `answer_type` (
                               `answer_type_id` INT NOT NULL AUTO_INCREMENT COMMENT '답안 유형 ID',
                               `description`    VARCHAR(30) NOT NULL COMMENT '문제 유형 설명',
                               PRIMARY KEY (`answer_type_id`)
) ENGINE=InnoDB
  DEFAULT CHARSET=utf8mb4
  COLLATE=utf8mb4_general_ci
  COMMENT='문제 답안 유형 코드 테이블';


-- 분야 테이블 생성
CREATE TABLE `category` (
                            `category_id`        INT NOT NULL AUTO_INCREMENT COMMENT '분야 ID',
                            `parent_category_id` INT NULL COMMENT '상위 분야 ID',
                            `name`               VARCHAR(30) NOT NULL COMMENT '분야 이름',
                            `description`       VARCHAR(255) COMMENT '분야 설명',
                            `time_limit`         INT NOT NULL COMMENT '제한 시간',
                            PRIMARY KEY (`category_id`),
                            CHECK (`time_limit` % 5 = 0)
    ) ENGINE=InnoDB
  DEFAULT CHARSET=utf8mb4
  COLLATE=utf8mb4_general_ci
  COMMENT='문제 분야 코드 테이블';


-- 사용자 테이블 생성
CREATE TABLE `user` (
                        `user_id`    INT NOT NULL AUTO_INCREMENT COMMENT '회원 Uid',
                        `account_id` VARCHAR(30) NOT NULL COMMENT '회원 아이디',
                        `password`   VARCHAR(255) NOT NULL COMMENT '암호화된 비밀번호',
                        `name`       VARCHAR(20) NOT NULL COMMENT '이름',
                        `gender`     ENUM('M','F') NOT NULL COMMENT '성별',
                        `birthdate`  DATE NOT NULL COMMENT '생년월일',
                        `point`      INT NOT NULL DEFAULT 0 COMMENT '포인트',
                        `ai_text`    TEXT NULL COMMENT 'AI 총평',
                        `authority`  ENUM('ADMIN','USER') NOT NULL DEFAULT 'USER' COMMENT '권한',
                        `is_deleted` ENUM('Y','N') NOT NULL DEFAULT 'N' COMMENT '삭제 여부',
                        `deleted_at` DATETIME NULL COMMENT '삭제 일시',
                        PRIMARY KEY (`user_id`)
) ENGINE=InnoDB
  DEFAULT CHARSET=utf8mb4
  COLLATE=utf8mb4_general_ci
  COMMENT='회원 정보 테이블';
  
-- 포인트 내역 테이블 생성
CREATE TABLE `point_history` (
    `point_id`     INT NOT NULL AUTO_INCREMENT COMMENT '포인트 ID',
    `user_id`      INT COMMENT '회원 Uid',
    `created_at`   DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '일시',
    `point`        INT NOT NULL COMMENT '포인트',
    `point_type`   ENUM('SAVE', 'SPEND') NOT NULL COMMENT '유형',
    PRIMARY KEY (`point_id`)
) ENGINE=InnoDB
  DEFAULT CHARSET=utf8mb4
  COLLATE=utf8mb4_general_ci
  COMMENT='포인트의 내역을 담은 테이블';


-- 검사 결과 테이블 생성
CREATE TABLE `test_result` (
                               `test_result_id`       INT NOT NULL AUTO_INCREMENT COMMENT '검사 결과 ID',
                               `user_id`              INT COMMENT '사용자 ID',
                               `lang_comp`            INT NOT NULL COMMENT '언어이해 총점',
                               `perc_reason`          INT NOT NULL COMMENT '지각추론 총점',
                               `work_memory`          INT NOT NULL COMMENT '작업기억 총점',
                               `proc_speed`           INT NOT NULL COMMENT '처리속도 총점',
                               `general_knowledge`    INT NOT NULL COMMENT '시사상식 총점',
                               `spatial_perception`   INT NOT NULL COMMENT '공간지각력 총점',
                               `ai_text`              TEXT NOT NULL COMMENT 'AI 총평',
                               `created_at`           DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '검사 일시',
                               `is_saved` ENUM('Y','N') NOT NULL DEFAULT 'N' COMMENT '저장 여부',
                               PRIMARY KEY (`test_result_id`)
) ENGINE=InnoDB
  DEFAULT CHARSET=utf8mb4
  COLLATE=utf8mb4_general_ci
  COMMENT='사용자 검사 결과 테이블';


-- 검사 문제 결과 테이블 생성
CREATE TABLE `test_problem` (
                                `test_problem_id` INT NOT NULL AUTO_INCREMENT COMMENT '검사 문제 ID',
                                `test_result_id`  INT COMMENT '검사 결과 ID',
                                `problem_id`      INT NOT NULL COMMENT '문제 ID',
                                `is_correct`      ENUM('Y','N') NOT NULL DEFAULT 'Y' COMMENT '정답 여부',
                                `answer`          VARCHAR(255) NOT NULL COMMENT '제출 정답',
                                PRIMARY KEY (`test_problem_id`)
) ENGINE=InnoDB
  DEFAULT CHARSET=utf8mb4
  COLLATE=utf8mb4_general_ci
  COMMENT='사용자 검사 시 문제별 정답 결과 테이블';


-- 문제 테이블 생성
CREATE TABLE `problem` (
                           `problem_id`        INT NOT NULL AUTO_INCREMENT COMMENT '문제 ID',
                           `category_id`       INT NULL COMMENT '분야 ID',
                           `answer_type_id`    INT NULL COMMENT '답안 유형 ID',
                           `content_image_url` VARCHAR(2048) NOT NULL COMMENT '본문 이미지 URL',
                           `correct_answer`    VARCHAR(255) NOT NULL COMMENT '정답',
                           `is_deleted`        ENUM('Y','N') NOT NULL DEFAULT 'N' COMMENT '삭제 여부',
                           `level`             ENUM('EASY','MEDIUM','HARD') NOT NULL DEFAULT 'EASY' COMMENT '난이도',
                           PRIMARY KEY (`problem_id`)
) ENGINE=InnoDB
  DEFAULT CHARSET=utf8mb4
  COLLATE=utf8mb4_general_ci
  COMMENT='문제 정보 테이블';

-- 학습 테이블 생성
CREATE TABLE `study` (
                         `study_id`   INT NOT NULL AUTO_INCREMENT COMMENT '학습 ID',
                         `user_id`    INT NULL COMMENT '사용자 ID',
                         `created_at` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '풀이 일시',
                         PRIMARY KEY (`study_id`)
) ENGINE=InnoDB
  DEFAULT CHARSET=utf8mb4
  COLLATE=utf8mb4_general_ci
  COMMENT='사용자의 문제 풀이 학습 이력 테이블';

-- 학습 결과 테이블 생성
CREATE TABLE `study_result` (
                                `study_result_id` INT NOT NULL AUTO_INCREMENT COMMENT '학습 결과 ID',
                                `study_id`        INT COMMENT '학습 ID',
                                `problem_id`      INT NOT NULL COMMENT '문제 ID',
                                `is_correct`      ENUM('Y','N') NOT NULL DEFAULT 'Y' COMMENT '정답 여부',
                                `answer`          VARCHAR(255) NOT NULL DEFAULT '' COMMENT '제출 정답',
                                PRIMARY KEY (`study_result_id`)
) ENGINE=InnoDB
  DEFAULT CHARSET=utf8mb4
  COLLATE=utf8mb4_general_ci
  COMMENT='사용자의 문제 풀이 학습 결과 테이블';


-- 이의 제기 테이블 생성
CREATE TABLE `objection` (
                             `objection_id`    INT NOT NULL AUTO_INCREMENT COMMENT '이의 제기 ID',
                             `user_id`         INT COMMENT '사용자 ID',
                             `problem_id`      INT NOT NULL COMMENT '문제 ID',
                             `status`          ENUM('PENDING','ACCEPTED','REJECTED') NOT NULL DEFAULT 'PENDING' COMMENT '상태',
                             `reason`          VARCHAR(255) NOT NULL COMMENT '이의 제기 사유',
                             `created_at`      DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '이의 제기 일시',
                             `processed_at`    DATETIME NULL COMMENT '처리 완료 일시',
                             `information` VARCHAR(255) NULL COMMENT '거절 사유',
                             PRIMARY KEY (`objection_id`)
) ENGINE=InnoDB
  DEFAULT CHARSET=utf8mb4
  COLLATE=utf8mb4_general_ci
  COMMENT='사용자 제재 이의 제기 이력 테이블';



-- 상위 분야 ID 외래 키 제약 조건 추가 (Self Join)
ALTER TABLE `category`
    ADD CONSTRAINT `fk_category_parent`
        FOREIGN KEY (`parent_category_id`)
            REFERENCES `category`(`category_id`)
            ON DELETE RESTRICT
            ON UPDATE CASCADE;


-- 사용자 ID 외래 키 제약 조건 추가
ALTER TABLE `point_history`
    ADD CONSTRAINT `fk_point_history_user`
        FOREIGN KEY (`user_id`)
            REFERENCES `user`(`user_id`)
            ON DELETE SET NULL
            ON UPDATE CASCADE;

-- 사용자 ID 외래 키 제약 조건 추가
ALTER TABLE `test_result`
    ADD CONSTRAINT `fk_test_result_user`
        FOREIGN KEY (`user_id`)
            REFERENCES `user`(`user_id`)
            ON DELETE SET NULL
            ON UPDATE CASCADE;

-- 검사 결과 ID 외래 키 제약 조건 추가
ALTER TABLE `test_problem`
    ADD CONSTRAINT `fk_test_problem_result`
        FOREIGN KEY (`test_result_id`)
            REFERENCES `test_result`(`test_result_id`)
            ON DELETE SET NULL
            ON UPDATE CASCADE;

-- 문제 ID 외래 키 제약 조건 추가
ALTER TABLE `test_problem`
    ADD CONSTRAINT `fk_test_problem_problem`
        FOREIGN KEY (`problem_id`)
            REFERENCES `problem`(`problem_id`)
            ON DELETE CASCADE
            ON UPDATE CASCADE;

-- 분야 ID 외래 키 제약 조건 추가
ALTER TABLE `problem`
    ADD CONSTRAINT `fk_problem_category`
        FOREIGN KEY (`category_id`)
            REFERENCES `category`(`category_id`)
            ON DELETE RESTRICT
            ON UPDATE CASCADE;

-- 답안 유형 ID 외래 키 제약 조건 추가
ALTER TABLE `problem`
    ADD CONSTRAINT `fk_problem_answer_type`
        FOREIGN KEY (`answer_type_id`)
            REFERENCES `answer_type`(`answer_type_id`)
    ON DELETE SET NULL
    ON UPDATE CASCADE;

-- 사용자 ID 외래 키 제약 조건 추가
ALTER TABLE `study`
    ADD CONSTRAINT `fk_study_user`
        FOREIGN KEY (`user_id`)
            REFERENCES `user`(`user_id`)
            ON DELETE SET NULL
            ON UPDATE CASCADE;

-- 학습 ID 외래 키 제약 조건 추가
ALTER TABLE `study_result`
    ADD CONSTRAINT `fk_study_result_study`
        FOREIGN KEY (`study_id`)
            REFERENCES `study`(`study_id`)
            ON DELETE SET NULL
            ON UPDATE CASCADE;

-- 문제 ID 외래 키 제약 조건 추가
ALTER TABLE `study_result`
    ADD CONSTRAINT `fk_study_result_problem`
        FOREIGN KEY (`problem_id`)
            REFERENCES `problem`(`problem_id`)
            ON DELETE CASCADE
            ON UPDATE CASCADE;


-- 사용자 ID 외래 키 제약 조건 추가
ALTER TABLE `objection`
    ADD CONSTRAINT `fk_objection_user`
        FOREIGN KEY (`user_id`)
            REFERENCES `user`(`user_id`)
            ON DELETE SET NULL
            ON UPDATE CASCADE;

-- 문제 ID 외래 키 제약 조건 추가
ALTER TABLE `objection`
    ADD CONSTRAINT `fk_objection_problem`
        FOREIGN KEY (`problem_id`)
            REFERENCES `problem`(`problem_id`)
            ON DELETE CASCADE
            ON UPDATE CASCADE;
