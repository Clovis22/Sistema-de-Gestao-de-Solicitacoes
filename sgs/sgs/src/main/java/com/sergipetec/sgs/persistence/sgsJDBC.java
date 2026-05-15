package com.sergipetec.sgs.persistence;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import org.springframework.jdbc.core.RowMapper;
import org.springframework.lang.NonNull;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import com.sergipetec.sgs.dtos.categoriaDTO.responseCategoriaDTO;
import com.sergipetec.sgs.dtos.solicitacaoDTO.createSolicitacaoDTO;
import com.sergipetec.sgs.dtos.solicitacaoDTO.responseSolicitacaoDTO;
import com.sergipetec.sgs.dtos.solicitacaoDTO.responseStatusSolicitacaoDTO;
import com.sergipetec.sgs.dtos.solicitanteDTO.responseSolicitanteDTO;
import com.sergipetec.sgs.dtos.statusDTO.responseStatusDTO;
import com.sergipetec.sgs.repository.sgsRepository;
import com.sergipetec.sgs.entity.categoria;
import com.sergipetec.sgs.entity.solicitacao;
import com.sergipetec.sgs.entity.solicitante;
import com.sergipetec.sgs.entity.status;

@Repository
public class sgsJDBC implements sgsRepository {

    private JdbcTemplate jdbcTemplate;

    public sgsJDBC(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public void cadastrarSolicitacao(createSolicitacaoDTO requestSolicitacao) {

        String query = "insert into public.solicitacao (" +
                            "solicitante_id, " +
                            "categoria_id, " +
                            "status_id, " +
                            "descricao, " +
                            "valor, " +
                            "data_solicitacao" +
                        ") values (?, ?, ?, ?, ?, ?)";

        jdbcTemplate.update(query,
            requestSolicitacao.solicitanteId(),
            requestSolicitacao.categoriaId(),
            requestSolicitacao.statusId(),
            requestSolicitacao.descricao(),
            requestSolicitacao.valor(),
            LocalDate.parse(requestSolicitacao.dataSolicitacao())
        );

    }
    
    public responseSolicitacaoDTO buscarPorId(Integer id) {

        String query = "select s.id, st.nome as NomeSolicitante, st.cpfcnpj, c.nome as NomeCategoria, stt.descricao as Status, valor from public.solicitacao s " +
                       "inner join public.solicitante st on st.id = s.solicitante_id " +
                       "inner join public.categoria c on c.id = s.categoria_id " +
                       "inner join public.status stt on stt.id = s.status_id " +
                       "where s.id = ? ";

        solicitacao objQuerySolicitacao = jdbcTemplate.queryForObject(
            query,
            new RowMapper<solicitacao>() {
                @Override
                public solicitacao mapRow(@NonNull ResultSet rs, int numeroLinhas) throws SQLException {

                    solicitacao objSolicitacao = new solicitacao();

                    objSolicitacao.setId(rs.getInt("id"));

                    // solicitante
                    solicitante objSolicitante = new solicitante(null, rs.getString("nomeSolicitante"), rs.getString("cpfcnpj"));

                    //categoria
                    categoria objCategoria = new categoria(null, rs.getString("nomeCategoria"));

                    // status
                    status objStatus = new status(null, rs.getString("Status"));

                    objSolicitacao.setSolicitante(objSolicitante);
                    objSolicitacao.setCategoria(objCategoria);
                    objSolicitacao.setStatus(objStatus);

                    objSolicitacao.setValor(rs.getFloat("valor"));

                    return objSolicitacao;

                }
            },
            id
        );

        if (objQuerySolicitacao == null) {
            throw new RuntimeException("Solicitação não encontrada!");
        }

        // conversão Entity -> DTO
        return new responseSolicitacaoDTO(
            objQuerySolicitacao.getId(),
            objQuerySolicitacao.getSolicitante().getNome(),
            objQuerySolicitacao.getSolicitante().getCpfcnpj(),
            objQuerySolicitacao.getCategoria().getNome(),
            objQuerySolicitacao.getStatus().getDescricao(),
            objQuerySolicitacao.getValor().floatValue()
        );
        
    }

    public responseStatusSolicitacaoDTO buscarStatusPorIdSolicitacao(Integer id) {

        String query = "select s.id, stt.descricao as Status, valor from public.solicitacao s " +
                       "inner join public.status stt on stt.id = s.status_id " +
                       "where s.id = ? ";

        solicitacao objQuerySolicitacao = jdbcTemplate.queryForObject(
            query,
            new RowMapper<solicitacao>() {
                @Override
                public solicitacao mapRow(@NonNull ResultSet rs, int numeroLinhas) throws SQLException {

                    solicitacao objSolicitacao = new solicitacao();

                    objSolicitacao.setId(rs.getInt("id"));

                    // status
                    status objStatus = new status(null, rs.getString("Status"));

                    objSolicitacao.setStatus(objStatus);

                    return objSolicitacao;

                }
            },
            id
        );

        if (objQuerySolicitacao == null) {
            throw new RuntimeException("Solicitação não encontrada!");
        }

        // conversão Entity -> DTO
        return new responseStatusSolicitacaoDTO(
            objQuerySolicitacao.getId(),
            objQuerySolicitacao.getStatus().getDescricao()
        );
        
    }

    public List<responseSolicitacaoDTO> listarSolicitacoes() {

        String query = "select s.id, st.nome as NomeSolicitante, st.cpfcnpj, c.nome as NomeCategoria, stt.descricao as Status, valor from public.solicitacao s " +
                       "inner join public.solicitante st on st.id = s.solicitante_id " +
                       "inner join public.categoria c on c.id = s.categoria_id " +
                       "inner join public.status stt on stt.id = s.status_id " +
                       "order by s.data_solicitacao desc";

        List<solicitacao> objQuerySolicitacoes = jdbcTemplate.query(
            query,
            new RowMapper<solicitacao>() {
                @Override
                public solicitacao mapRow(@NonNull ResultSet rs, int numeroLinhas) throws SQLException {

                    solicitacao objSolicitacao = new solicitacao();

                    objSolicitacao.setId(rs.getInt("id"));

                    // solicitante
                    solicitante objSolicitante = new solicitante(null, rs.getString("nomeSolicitante"), rs.getString("cpfcnpj"));

                    //categoria
                    categoria objCategoria = new categoria(null, rs.getString("nomeCategoria"));

                    // status
                    status objStatus = new status(null, rs.getString("Status"));

                    objSolicitacao.setSolicitante(objSolicitante);
                    objSolicitacao.setCategoria(objCategoria);
                    objSolicitacao.setStatus(objStatus);

                    objSolicitacao.setValor(rs.getFloat("valor"));

                    return objSolicitacao;

                }
            }
        );

        // conversão Entity -> DTO
        return objQuerySolicitacoes.stream()
            .map(s -> new responseSolicitacaoDTO(
                 s.getId(),
                 s.getSolicitante().getNome(),
                 s.getSolicitante().getCpfcnpj(),
                 s.getCategoria().getNome(),
                 s.getStatus().getDescricao(),
                 s.getValor()
            )
        ).toList();

    }

    public List<responseStatusDTO> listarStatus() {
        String query = "select id, descricao from public.status";

        List<status> objQueryStatus = jdbcTemplate.query(
            query,
            new RowMapper<status>() {
                @Override
                public status mapRow(@NonNull ResultSet rs, int numeroLinhas) throws SQLException {

                    status objStatus = new status();

                    objStatus.setId(rs.getInt("id"));

                    objStatus.setDescricao(rs.getString("descricao"));

                    return objStatus;

                }
            }
        );

        if (objQueryStatus.isEmpty()) {
            throw new RuntimeException("Status não encontrados!");
        }

        // conversão Entity -> DTO
        return objQueryStatus.stream()
            .map(s -> new responseStatusDTO(
                 s.getId(),
                 s.getDescricao()
            )
        ).toList();
    }

    public List<responseCategoriaDTO> listarCategorias() {
        String query = "select id, nome from public.categoria";

        List<categoria> objQueryCategoria = jdbcTemplate.query(
            query,
            new RowMapper<categoria>() {
                @Override
                public categoria mapRow(@NonNull ResultSet rs, int numeroLinhas) throws SQLException {

                    categoria objCategoria = new categoria();

                    objCategoria.setId(rs.getInt("id"));

                    objCategoria.setNome(rs.getString("nome"));

                    return objCategoria;

                }
            }
        );

        if (objQueryCategoria.isEmpty()) {
            throw new RuntimeException("Categorias não encontradas!");
        }

        // conversão Entity -> DTO
        return objQueryCategoria.stream()
            .map(s -> new responseCategoriaDTO(
                 s.getId(),
                 s.getNome()
            )
        ).toList();
    }

    public List<responseSolicitanteDTO> listarSolicitantes() {
        String query = "select id, nome from public.solicitante";

        List<solicitante> objQuerySolicitante = jdbcTemplate.query(
            query,
            new RowMapper<solicitante>() {
                @Override
                public solicitante mapRow(@NonNull ResultSet rs, int numeroLinhas) throws SQLException {

                    solicitante objSolicitante = new solicitante();

                    objSolicitante.setId(rs.getInt("id"));

                    objSolicitante.setNome(rs.getString("nome"));

                    return objSolicitante;

                }
            }
        );

        if (objQuerySolicitante.isEmpty()) {
            throw new RuntimeException("Solicitantes não encontrados!");
        }

        // conversão Entity -> DTO
        return objQuerySolicitante.stream()
            .map(s -> new responseSolicitanteDTO(
                 s.getId(),
                 s.getNome()
            )
        ).toList();
    }

    public List<responseSolicitacaoDTO> buscarPorFiltro(String status, String categoria, LocalDate dataInicial, LocalDate dataFinal) {

        StringBuilder query = new StringBuilder();

        query.append("select s.id, st.nome as NomeSolicitante, st.cpfcnpj, c.nome as NomeCategoria, stt.descricao as Status, " +
                     "s.valor, s.data_solicitacao from public.solicitacao s " +
                     "inner join public.solicitante st on st.id = s.solicitante_id " +
                     "inner join public.categoria c on c.id = s.categoria_id " +
                     "inner join public.status stt on stt.id = s.status_id " +
                     "where 1 = 1 "
        );

        List<Object> parametros = new ArrayList<>();

        String queryBuscarStatusPorId = "select descricao from public.status "+
                                        "where id = ?";

        String statusAux = null;
        if(isNumeric(status)) {
           statusAux = jdbcTemplate.queryForObject(
                queryBuscarStatusPorId,
                String.class,
                Integer.parseInt(status)
           );
           status = statusAux;
        }

        String queryBuscarCategoriaPorId = "select nome from public.categoria "+
                                           "where id = ?";

        String categoriaAux = null;
         if(isNumeric(categoria)) {
            categoriaAux = jdbcTemplate.queryForObject(
                queryBuscarCategoriaPorId,
                String.class,
                Integer.parseInt(categoria)
            );
            categoria = categoriaAux;
        }

        // filtro status
        if(status != null && !status.isBlank()) {
           query.append(" and stt.descricao = ? ");
           parametros.add(status);
        }

        // filtro categoria
        if(categoria != null && !categoria.isBlank()) {
           query.append(" and c.nome = ? ");
           parametros.add(categoria);
        }

        // filtro data inicial
        if(dataInicial != null) {
           query.append(" and s.data_solicitacao >= ? ");
           parametros.add(dataInicial);
        }

        // filtro data final
        if(dataFinal != null) {
           query.append(" and s.data_solicitacao <= ? ");
           parametros.add(dataFinal);
        }

        // ordenação
        query.append(" order by s.data_solicitacao desc ");
        String sqlQuery = Objects.requireNonNull(query.toString());
        
        List<solicitacao> listaSolicitacoes = jdbcTemplate.query(
            sqlQuery,
            new RowMapper<solicitacao>() {
                @Override
                public solicitacao mapRow(@NonNull ResultSet rs, int numeroLinhas) throws SQLException {

                    solicitacao objSolicitacao = new solicitacao();

                    objSolicitacao.setId(rs.getInt("id"));

                    // solicitante
                    solicitante objSolicitante = new solicitante(null, rs.getString("NomeSolicitante"), rs.getString("cpfcnpj"));

                    // categoria 
                    categoria objCategoria = new categoria(null, rs.getString("NomeCategoria"));

                    // status
                    status objStatus = new status(null, rs.getString("Status"));

                    // monta entity
                    objSolicitacao.setSolicitante(objSolicitante);
                    objSolicitacao.setCategoria(objCategoria);
                    objSolicitacao.setStatus(objStatus);
                    objSolicitacao.setValor(rs.getFloat("valor"));

                    return objSolicitacao;
                }
            },
            parametros.toArray()
        );

        // conversão Entity -> DTO
        return listaSolicitacoes.stream()
            .map(s -> new responseSolicitacaoDTO(
                 s.getId(),
                 s.getSolicitante().getNome(),
                 s.getSolicitante().getCpfcnpj(),
                 s.getCategoria().getNome(),
                 s.getStatus().getDescricao(),
                 s.getValor()
            )
        ).toList();
    }

    public void atualizarStatus(Integer id, Integer statusId) {

        // buscando o status atual
        String queryBuscarStatusAtual = "select stt.descricao from public.solicitacao s " +
                                        "inner join public.status stt on stt.id = s.status_id " +
                                        "where s.id = ?";

        String queryBuscarStatusPorId = "select descricao from public.status "+
                                        "where id = ?";

        String statusAtual = jdbcTemplate.queryForObject(
            queryBuscarStatusAtual,
            String.class,
            id
        );

         String novoStatus = jdbcTemplate.queryForObject(
            queryBuscarStatusPorId,
            String.class,
            statusId
        );

        if(statusAtual == null)
           throw new RuntimeException("O Status em solicitação não foi encontrado!");

        if(novoStatus == null)
           throw new RuntimeException("O novo Status não foi encontrado!");

        // removendo espaçamentos
        statusAtual = statusAtual.toUpperCase().trim();
        novoStatus = novoStatus.toUpperCase().trim();

        // validando as transições
        boolean transicaoValidada = false;
        switch(statusAtual) {
            case "SOLICITADO":
                if(novoStatus.equals("LIBERADO") || novoStatus.equals("REJEITADO"))
                   transicaoValidada = true;
            break;
            case "LIBERADO":
                if(novoStatus.equals("APROVADO") || novoStatus.equals("REJEITADO"))
                   transicaoValidada = true;
            break;
            case "APROVADO":
                if(novoStatus.equals("CANCELADO"))
                   transicaoValidada = true;
            break;
            case "REJEITADO":
            case "CANCELADO":
                throw new RuntimeException("O status não foi encontrado para altualização!");
            default:
                throw new RuntimeException("O status atual é inválido!");
        }

        if(!transicaoValidada)
           throw new RuntimeException("Transição inválida: " + statusAtual + " -> " + novoStatus);

        // buscando o id do novo status
        String queryBuscarIdStatus = "select id from public.status " +
                                     "where upper(descricao) = upper(?)";

        Integer idNovoStatus = jdbcTemplate.queryForObject(
            queryBuscarIdStatus,
            Integer.class,
            novoStatus
        );

        if(idNovoStatus == null)
           throw new RuntimeException("O novo status não foi encontrado!");

        // alterar id do status na tabela solicitacao
        String queryUpdate = "update public.solicitacao " +
                             "set status_id = ? " +
                             "where id = ?";

        jdbcTemplate.update(
            queryUpdate,
            idNovoStatus,
            id
        );
    } 
    
    public static boolean isNumeric(String value) {
        if(value == null) return false;
        try {
            Double.parseDouble(value);
            return true;
        } catch(NumberFormatException e) {
            return false;
        }
    }

}
