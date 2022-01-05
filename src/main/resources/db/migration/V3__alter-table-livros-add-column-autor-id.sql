alter table livros add column cadastro_autor_id bigint not null;
alter table livros add foreign key (cadastro_autor_id) references autores(id);