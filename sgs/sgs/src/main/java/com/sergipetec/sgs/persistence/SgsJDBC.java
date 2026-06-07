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

import com.sergipetec.sgs.dtos.CategoriaDTO.ResponseCategoriaDTO;
import com.sergipetec.sgs.dtos.SolicitacaoDTO.CreateSolicitacaoDTO;
import com.sergipetec.sgs.dtos.SolicitacaoDTO.ResponseSolicitacaoDTO;
import com.sergipetec.sgs.dtos.SolicitanteDTO.ResponseSolicitanteDTO;
import com.sergipetec.sgs.dtos.StatusDTO.ResponseStatusDTO;
import com.sergipetec.sgs.repository.SgsRepository;
import com.sergipetec.sgs.utils.NumericUtils;
import com.sergipetec.sgs.entity.Categoria;
import com.sergipetec.sgs.entity.Solicitacao;
import com.sergipetec.sgs.entity.Solicitante;
import com.sergipetec.sgs.entity.Status;

@Repository
public class SgsJDBC implements SgsRepository {

    private JdbcTemplate jdbcTemplate;

    public SgsJDBC(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public void cadastrarSolicitacao(CreateSolicitacaoDTO requestSolicitacao) {

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
    
    public ResponseSolicitacaoDTO buscarPorId(Integer id) {

        String query = "select s.id, st.nome as NomeSolicitante, st.cpfcnpj, c.nome as NomeCategoria, stt.descricao as Status, valor from public.solicitacao s " +
                       "inner join public.solicitante st on st.id = s.solicitante_id " +
                       "inner join public.categoria c on c.id = s.categoria_id " +
                       "inner join public.status stt on stt.id = s.status_id " +
                       "where s.id = ? ";

        Solicitacao objQuerySolicitacao = jdbcTemplate.queryForObject(
            query,
            new RowMapper<Solicitacao>() {
                @Override
                public Solicitacao mapRow(@NonNull ResultSet rs, int numeroLinhas) throws SQLException {

                    Solicitacao objSolicitacao = new Solicitacao();

                    objSolicitacao.setId(rs.getInt("id"));

                    // solicitante
                    Solicitante objSolicitante = new Solicitante(null, rs.getString("nomeSolicitante"), rs.getString("cpfcnpj"));

                    //categoria
                    Categoria objCategoria = new Categoria(null, rs.getString("nomeCategoria"));

                    // status
                    Status objStatus = new Status(null, rs.getString("Status"));

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
        return new ResponseSolicitacaoDTO(
            objQuerySolicitacao.getId(),
            objQuerySolicitacao.getSolicitante().getNome(),
            objQuerySolicitacao.getSolicitante().getCpfcnpj(),
            objQuerySolicitacao.getCategoria().getNome(),
            objQuerySolicitacao.getStatus().getDescricao(),
            objQuerySolicitacao.getValor().floatValue()
        );
        
    }

    public List<ResponseSolicitacaoDTO> listarSolicitacoes() {

        String query = "select s.id, st.nome as NomeSolicitante, st.cpfcnpj, c.nome as NomeCategoria, stt.descricao as Status, valor from public.solicitacao s " +
                       "inner join public.solicitante st on st.id = s.solicitante_id " +
                       "inner join public.categoria c on c.id = s.categoria_id " +
                       "inner join public.status stt on stt.id = s.status_id " +
                       "order by s.data_solicitacao desc";

        List<Solicitacao> objQuerySolicitacoes = jdbcTemplate.query(
            query,
            new RowMapper<Solicitacao>() {
                @Override
                public Solicitacao mapRow(@NonNull ResultSet rs, int numeroLinhas) throws SQLException {

                    Solicitacao objSolicitacao = new Solicitacao();

                    objSolicitacao.setId(rs.getInt("id"));

                    // solicitante
                    Solicitante objSolicitante = new Solicitante(null, rs.getString("nomeSolicitante"), rs.getString("cpfcnpj"));

                    //categoria
                    Categoria objCategoria = new Categoria(null, rs.getString("nomeCategoria"));

                    // status
                    Status objStatus = new Status(null, rs.getString("Status"));

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
            .map(s -> new ResponseSolicitacaoDTO(
                 s.getId(),
                 s.getSolicitante().getNome(),
                 s.getSolicitante().getCpfcnpj(),
                 s.getCategoria().getNome(),
                 s.getStatus().getDescricao(),
                 s.getValor()
            )
        ).toList();

    }

    public List<ResponseStatusDTO> listarStatus() {
        String query = "select id, descricao from public.status";

        List<Status> objQueryStatus = jdbcTemplate.query(
            query,
            new RowMapper<Status>() {
                @Override
                public Status mapRow(@NonNull ResultSet rs, int numeroLinhas) throws SQLException {

                    Status objStatus = new Status();

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
            .map(s -> new ResponseStatusDTO(
                 s.getId(),
                 s.getDescricao()
            )
        ).toList();
    }

    public List<ResponseCategoriaDTO> listarCategorias() {
        String query = "select id, nome from public.categoria";

        List<Categoria> objQueryCategoria = jdbcTemplate.query(
            query,
            new RowMapper<Categoria>() {
                @Override
                public Categoria mapRow(@NonNull ResultSet rs, int numeroLinhas) throws SQLException {

                    Categoria objCategoria = new Categoria();

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
            .map(s -> new ResponseCategoriaDTO(
                 s.getId(),
                 s.getNome()
            )
        ).toList();
    }

    public List<ResponseSolicitanteDTO> listarSolicitantes() {
        String query = "select id, nome from public.solicitante";

        List<Solicitante> objQuerySolicitante = jdbcTemplate.query(
            query,
            new RowMapper<Solicitante>() {
                @Override
                public Solicitante mapRow(@NonNull ResultSet rs, int numeroLinhas) throws SQLException {

                    Solicitante objSolicitante = new Solicitante();

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
            .map(s -> new ResponseSolicitanteDTO(
                 s.getId(),
                 s.getNome()
            )
        ).toList();
    }

    public List<ResponseSolicitacaoDTO> buscarPorFiltro(String status, String categoria, LocalDate dataInicial, LocalDate dataFinal) {

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

        NumericUtils numericUtils = new NumericUtils();

        String statusAux = null;
        if(numericUtils.isNumeric(status)) {
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
         if(numericUtils.isNumeric(categoria)) {
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
        
        List<Solicitacao> listaSolicitacoes = jdbcTemplate.query(
            sqlQuery,
            new RowMapper<Solicitacao>() {
                @Override
                public Solicitacao mapRow(@NonNull ResultSet rs, int numeroLinhas) throws SQLException {

                    Solicitacao objSolicitacao = new Solicitacao();

                    objSolicitacao.setId(rs.getInt("id"));

                    // solicitante
                    Solicitante objSolicitante = new Solicitante(null, rs.getString("NomeSolicitante"), rs.getString("cpfcnpj"));

                    // categoria 
                    Categoria objCategoria = new Categoria(null, rs.getString("NomeCategoria"));

                    // status
                    Status objStatus = new Status(null, rs.getString("Status"));

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
            .map(s -> new ResponseSolicitacaoDTO(
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

}
