SELECT setval(pg_get_serial_sequence('tb_person','id'), (SELECT MAX(id) FROM tb_person));

