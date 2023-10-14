CREATE TABLE users (
    mid INTEGER primary key,
    name VARCHAR(20) not null,
    sex VARCHAR(10) not null,
    birthday DATE,
    level INTEGER not null,
    sign TEXT,
    fl_id INTEGER[],
    identity VARCHAR(20) not null
);

CREATE TABLE video_basic(
    BV VARCHAR(20) primary key,
    title VARCHAR(100) not null,
    owner_id INTEGER not null,
    commit_time DATE not null,
    review_time DATE not null,
    public_time DATE not null,
    duration INTEGER not null,
    description TEXT,
    reviewer_id INTEGER not null,
    like_id INTEGER[],
    coin_id INTEGER[],
    favorite_id INTEGER[]
);

CREATE TABLE video_view(
    BV VARCHAR(20) not null,
    user_id INTEGER not null,
    view_time INTEGER not null,
    primary key (BV, user_id)
);

CREATE TABLE content(
    BV VARCHAR(20) not null,
    user_id INTEGER not null,
    time INTEGER not null,
    content TEXT not null,
    primary key (BV, user_id, time)
);
