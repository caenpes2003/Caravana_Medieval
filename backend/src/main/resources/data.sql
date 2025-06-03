
-- ==========================
-- 🚩 CIUDADES
-- ==========================
INSERT INTO ciudad (nombre, impuesto_entrada) VALUES
  ('Lisboa', 2.5),
  ('Madrid', 3.0),
  ('Barcelona', 4.0),
  ('Paris', 5.0),
  ('Roma', 2.0),
  ('Londres', 3.5),
  ('Berlin', 2.8),
  ('Amsterdam', 1.9),  
  ('Bruselas', 2.6),
  ('Praga', 3.2);

-- ==========================
-- 📦 PRODUCTOS
-- ==========================
INSERT INTO producto (id, nombre) VALUES
  (1, 'Especias'),
  (2, 'Telas'),
  (3, 'Armas'),
  (4, 'Metales'),
  (5, 'Ganado'),
  (6, 'Vino'),
  (7, 'Libros'),
  (8, 'Joyeria');

-- ==========================
-- 🏪 STOCK
-- ==========================
INSERT INTO stock (id, cantidad, FD, FO, ciudad_id, producto_id) VALUES
  (1, 60, 1.2, 0.6, 1, 1),
  (2, 35, 1.1, 0.7, 1, 2),
  (3, 20, 1.5, 0.8, 1, 3),
  (4, 45, 1.3, 0.5, 2, 4),
  (5, 30, 1.4, 0.9, 2, 1),
  (6, 25, 1.2, 0.6, 3, 5),
  (7, 50, 1.1, 0.7, 3, 6),
  (8, 15, 1.6, 0.4, 3, 7),
  (9, 33, 1.2, 0.6, 4, 1),
  (10, 40, 1.3, 0.8,4, 2),
  (11, 26, 1.4, 0.7,5, 5),
  (12, 38, 1.2, 0.6,5, 8),
  (13, 22, 1.5, 0.9,6, 4),
  (14, 28, 1.4, 0.5,7, 3),
  (15, 35, 1.3, 0.6,8, 6),
  (16, 31, 1.3, 0.6,9, 1),
  (17, 27, 1.2, 0.7,9, 7),
  (18, 30, 1.4, 0.8,9, 2),
  (19, 25, 1.5, 0.6, 10, 8),
  (20, 34, 1.3, 0.5, 10, 3),
  (21, 29, 1.2, 0.7,10, 5);


INSERT INTO servicio_base (tipo, costo) VALUES
 ('Reparar',           20),
 ('MejorarCapacidad',  50),
 ('MejorarVelocidad',  30),
 ('Guardias',         100);

ALTER TABLE stock ALTER COLUMN id RESTART WITH 100;