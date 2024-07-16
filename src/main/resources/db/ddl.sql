CREATE TABLE IF NOT EXISTS users (
                       user_id INT PRIMARY KEY AUTO_INCREMENT,
                       username VARCHAR(50) NOT NULL UNIQUE,
                       password VARCHAR(255) NOT NULL,
                       email VARCHAR(100) NOT NULL UNIQUE,
                       name VARCHAR(100) NOT NULL,
                       created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE IF NOT EXISTS POSTS (
                       post_id BIGINT PRIMARY KEY AUTO_INCREMENT,
                       user_id BIGINT NOT NULL,
                       content VARCHAR(255) NOT NULL,
                       image_path VARCHAR(255) NOT NULL
);


CREATE TABLE IF NOT EXISTS comments (
                          comment_id INT PRIMARY KEY AUTO_INCREMENT,
                          post_id INT NOT NULL,
                          user_id INT NOT NULL,
                          content TEXT NOT NULL,
                          created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

INSERT INTO USERS (USER_ID, USERNAME, PASSWORD, EMAIL, NAME, CREATED_AT) VALUES
    (1, 'd', 'd', 'd@n', 'd', '2024-07-16 14:35:37.823662');

INSERT INTO POSTS (POST_ID, USER_ID, CONTENT, IMAGE_PATH) VALUES
    (1, 1, 'asdf', '3479a814-0272-49d8-8699-e318f23cc902.png');

INSERT INTO COMMENTS (COMMENT_ID, POST_ID, USER_ID, CONTENT, CREATED_AT) VALUES
     (1, 1, 1001, 'This is the first comment.', '2024-07-16 14:30:00'),
     (2, 1, 1002, 'This is the second comment.', '2024-07-16 14:31:00'),
     (3, 1, 1003, 'This is the third comment.', '2024-07-16 14:32:00');
