CREATE TABLE Account(
                        id INT GENERATED ALWAYS AS IDENTITY,
                        user_name VARCHAR(50) NOT NULL,
                        password VARCHAR(50) NOT NULL,
                        PRIMARY KEY(id)
);

CREATE TABLE Chat(
                     id INT GENERATED ALWAYS AS IDENTITY,
                     PRIMARY KEY(id)
);

CREATE TABLE Account_Chat(
                             id INT GENERATED ALWAYS AS IDENTITY,
                             account_id INT,
                             chat_id INT,
                             PRIMARY KEY(id),
                             CONSTRAINT fk_account
                                 FOREIGN KEY(account_id)
                                     REFERENCES Account(id),
                             CONSTRAINT fk_chat
                                 FOREIGN KEY(chat_id)
                                     REFERENCES Chat(id)
);

CREATE TABLE Message(
                        id INT GENERATED ALWAYS AS IDENTITY,
                        body varchar(250),
                        sender_account_id INT,
                        chat_id INT,
                        PRIMARY KEY(id),
                        CONSTRAINT fk_account
                            FOREIGN KEY(sender_account_id)
                                REFERENCES Account(id),
                        CONSTRAINT fk_chat
                            FOREIGN KEY(chat_id)
                                REFERENCES Chat(id)
);