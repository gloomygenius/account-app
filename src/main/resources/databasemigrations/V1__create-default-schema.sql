CREATE table account
(
    number   INT PRIMARY KEY AUTO_INCREMENT,
    balance  NUMERIC(8, 2) NOT NULL,
    revision INT default 0 NOT NULL
);


CREATE table transaction
(
    id                      BIGINT PRIMARY KEY AUTO_INCREMENT,
    debtor_account_number   INT           NOT NULL,
    creditor_account_number INT           NOT NULL,
    amount                  numeric(8, 2) NOT NULL,
    foreign key (debtor_account_number) references account (number),
    foreign key (creditor_account_number) references account (number)
);