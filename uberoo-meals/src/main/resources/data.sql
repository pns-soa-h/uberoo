INSERT INTO TAG (id, label) VALUES
  (1, 'asian'),
  (2, 'french');

INSERT INTO RESTAURANT (id, name) VALUES
  (5, 'LE Resto');

INSERT INTO MEAL (id, label, description, tag_id) VALUES
  (3, 'Jambon-Beurre', 'Un sandwich, avec du jambon, et du beurre.', 2),
  (4, 'Ramen', 'Un bon ramen', 1);
