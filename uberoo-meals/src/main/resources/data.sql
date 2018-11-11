INSERT INTO TAG (id, label) VALUES
  (1, 'asian'),
  (2, 'french');

INSERT INTO RESTAURANT (id, name) VALUES
  (5, 'LE Resto'),
  (6, 'Asian Resto');

INSERT INTO MEAL (id, name, description, tag_id, restaurant_id, price) VALUES
  (3, 'Jambon-Beurre', 'Un sandwich, avec du jambon, et du beurre.', 2, 5, 3.0),
  (4, 'Ramen', 'Un bon ramen', 1, 6, 5.0);
