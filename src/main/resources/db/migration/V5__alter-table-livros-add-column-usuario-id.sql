alter table livros add column usuario_id bigint;
alter table livros add foreign key(usuario_id) references usuarios(id);