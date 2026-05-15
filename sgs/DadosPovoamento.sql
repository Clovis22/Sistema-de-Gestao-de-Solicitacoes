Insert into public.solicitacao(solicitante_id, 
							   categoria_id, 
							   descricao, 
							   valor, 
							   data_solicitacao, 
							   status_id) 
							   values (1, 
									   1, 
									   'Manutenção de uma lâmpada no apartamento número 200',
										100,
										'15/05/2026',
										1);

Insert into public.solicitante(id, nome, cpfcnpj) values (1,'João da Silva','02547865425');
Insert into public.solicitante(id, nome, cpfcnpj) values (2,'Mariane Paulista Santana','68547125793');
Insert into public.solicitante(id, nome, cpfcnpj) values (3,'João Deolindo da Cruz','25789635475');
Insert into public.solicitante(id, nome, cpfcnpj) values (4,'Flávio Campos da Silva','22588744698');
Insert into public.solicitante(id, nome, cpfcnpj) values (5,'Raquel de Oliveira dos Santos','99674405216');

Insert into public.categoria(id, nome) values (1, 'SERVIÇOS'),
                                              (2, 'MATERIAL'),
										      (3, 'TRANSPORTE'),
											  (4, 'MANUTENÇÃO'),
											  (5, 'TECNOLOGIA');
											  
											  
Insert into public.status(id, descricao) values (1, 'SOLICITADO'),
                                                (2, 'LIBERADO'),
												(3, 'APROVADO'),
												(4, 'REJEITADO'),
												(5, 'CANCELADO');

Select * From public.categoria
Select * From public.solicitacao
Select * From public.solicitante
Select * From public.status
