create table p_hubs
(
    is_deleted bit            not null comment '삭제여부',
    latitude   decimal(10, 7) not null comment '위도',
    longitude  decimal(10, 7) not null comment '경도',
    created_at datetime(6)    null comment '레코드 생성 일시',
    deleted_at datetime(6)    null comment '레코드 삭제 일시',
    updated_at datetime(6)    null comment '레코드 수정 일시',
    hub_id     binary(16)     not null
        primary key,
    created_by varchar(50)    null comment '레코드 생성자',
    deleted_by varchar(50)    null comment '레코드 삭제자',
    updated_by varchar(50)    null comment '레코드 수정자',
    address    varchar(255)   not null comment '허브주소',
    name       varchar(255)   not null comment '허브명'
);

create table p_hub_routes
(
    is_deleted    bit            not null comment '삭제여부',
    move_distance decimal(10, 3) not null comment '이동거리',
    time_required time(6)        not null comment '소요시간',
    created_at    datetime(6)    null comment '레코드 생성 일시',
    deleted_at    datetime(6)    null comment '레코드 삭제 일시',
    updated_at    datetime(6)    null comment '레코드 수정 일시',
    from_hub_id   binary(16)     not null comment '도착지 허브 ID',
    hub_id        binary(16)     null,
    hub_route_id  binary(16)     not null
        primary key,
    to_hub_id     binary(16)     not null comment '출발지 허브 ID',
    created_by    varchar(50)    null comment '레코드 생성자',
    deleted_by    varchar(50)    null comment '레코드 삭제자',
    updated_by    varchar(50)    null comment '레코드 수정자',
    constraint FKqma7yjnwd00ourgq5r7xwgik3
        foreign key (hub_id) references p_hubs (hub_id)
);