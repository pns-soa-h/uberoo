INSERT INTO TAG (id, label) VALUES
  (1, 'asian'),
  (2, 'french');

INSERT INTO RESTAURANT (id, name) VALUES
  (5, 'LE Resto'),
  (6, 'Asian Resto');

INSERT INTO MEAL (id, label, price, description, restaurant_id, tag_id) VALUES
  (3, 'Jambon-Beurre', 4.99, 'Un sandwich, avec du jambon, et du beurre.', 5, 2),
  (4, 'Ramen', 8.99, 'Un bon ramen', 6, 1);
