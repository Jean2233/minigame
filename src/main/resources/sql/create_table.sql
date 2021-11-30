CREATE TABLE IF NOT EXISTS `minigame_accounts` (
    id CHAR(36) NOT NULL PRIMARY KEY,
    name VARCHAR(16) NOT NULL,
    last_score INT NOT NULL,
    total_score INT NOT NULL
);