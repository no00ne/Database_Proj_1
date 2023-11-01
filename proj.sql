create database project1;
create schema project1.project1;

-- 1. user的基本信息
CREATE TABLE users (
    mid BIGINT primary key,
    name VARCHAR not null,
    sex VARCHAR(10) not null,
    birthday VARCHAR,
    level INTEGER not null,
    sign TEXT,
    fl_id BIGINT[],
    identity VARCHAR(20) not null
);

-- 2. 视频的基本信息
CREATE TABLE video_basic(
    BV VARCHAR primary key,
    title VARCHAR not null,
    owner_id BIGINT not null,
    owner_name VARCHAR not null,
    commit_time VARCHAR not null,
    review_time VARCHAR not null,
    public_time VARCHAR not null,
    duration INTEGER not null,
    description TEXT,
    reviewer_id BIGINT
);

-- 3. 视频观看信息
CREATE TABLE video_view(
    BV VARCHAR not null references video_basic(BV),
    user_id BIGINT not null references users(mid),
    view_time INTEGER not null,
    primary key (BV, user_id)
);

-- 4. 弹幕信息
CREATE TABLE content(
    dm_id BIGINT primary key,
    BV VARCHAR not null references video_basic(BV),
    user_id BIGINT not null references users(mid),
    time DECIMAL not null,
    content TEXT not null
);


-- 5. 视频喜欢信息
CREATE TABLE like_id(
	BV VARCHAR not null references video_basic(BV),
	like_id BIGINT not null references users(mid),
	primary key (BV, like_id)
);
-- 6. 视频硬币信息
CREATE TABLE coin_id(
	BV VARCHAR not null references video_basic(BV),
	coin_id BIGINT not null references users(mid),
	primary key (BV, coin_id)
);
-- 7. 视频收藏信息
CREATE TABLE favorite_id(
	BV VARCHAR not null references video_basic(BV),
	favourite_id BIGINT not null references users(mid),
	primary key (BV, favourite_id)
);
