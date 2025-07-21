

-- V3__Sync_person_id_sequence.sql
-- Ajusta a sequência do id na tabela TB_PERSON após populações manuais

SELECT setval(
  pg_get_serial_sequence('TB_PERSON', 'id'),
  (SELECT MAX(id) FROM TB_PERSON)
);
