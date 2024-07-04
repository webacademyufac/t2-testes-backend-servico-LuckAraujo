package br.ufac.sgcmapi.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import br.ufac.sgcmapi.model.Atendimento;
import br.ufac.sgcmapi.model.EStatus;
import br.ufac.sgcmapi.model.Paciente;
import br.ufac.sgcmapi.model.Profissional;
import br.ufac.sgcmapi.repository.AtendimentoRepository;

@ExtendWith(MockitoExtension.class)
public class AtendimentoServiceTest {
    @Mock
    private AtendimentoRepository repo;

    @InjectMocks
    private AtendimentoService servico;

    Atendimento a1;
    Atendimento a2;
    Paciente p1;
    List<Atendimento> atendimentos;

    @BeforeEach
    public void setUp() {
        a1 = new Atendimento();
        a2 = new Atendimento();
        p1 = new Paciente();

        a1.setId(1L);
        a2.setId(2L);
        p1.setId(1L);

        a1.setHora(LocalTime.of(14, 00));
        a2.setHora(LocalTime.of(15, 00));
        p1.setNome("Ana");
        
        a1.setPaciente(p1);
        
        atendimentos = new ArrayList<>();
        atendimentos.add(a1);
        atendimentos.add(a2);
    }

    @Test //Luck1
    public void testeAtendimentoGetById() //Atividade
    {
        Mockito.when(repo.findById(1L)).thenReturn(Optional.of(a1));
        Atendimento result = servico.get(1L);
        assertEquals(1, result.getId());
    }

    @Test //Luck2
    void testAtendimentoTermoBusca() //Atividade
    {
        Mockito.when(repo.busca("Ana")).thenReturn(atendimentos);
        List<Atendimento> buscaResult = servico.get("Ana");
        Atendimento result = buscaResult.get(0);
        assertEquals("Ana", result.getPaciente().getNome());
    }

    @Test //Luck3
    void testSave() //Atividade
    {
        Mockito.when(repo.save(a1)).thenReturn(a1);
        assertEquals(a1, repo.save(a1));
    }

    @Test
    void testGetHorarios() {
        Mockito.when(repo.findByProfissionalAndDataAndStatusNot(
                Mockito.any(Profissional.class),
                Mockito.eq(LocalDate.now()),
                Mockito.eq(EStatus.CANCELADO))).thenReturn(atendimentos);

        List<String> result = servico.getHorarios(1L, LocalDate.now());
        assertEquals(2, result.size());
        assertTrue(result.contains("15:00:00"));
    }


    @Test
    public void testUpdateStatus() {
        Mockito.when(repo.findById(1L)).thenReturn(Optional.of(a1));
        Atendimento result = servico.updateStatus(1L);
        assertNotNull(result);
        assertEquals(EStatus.CONFIRMADO, result.getStatus());
    }

    @Test
    public void testAtendimentoDelete() {
        Mockito.doNothing().when(repo).deleteById(1L);
        repo.deleteById(1L);
        Mockito.verify(repo, Mockito.times(1)).deleteById(1L);
    }

    @Test
    public void testAtendimentoGetAll() {
        Mockito.when(repo.findAll()).thenReturn(atendimentos);
        List<Atendimento> result = servico.get();
        assertEquals(2, result.size());
        assertEquals(2L, result.get(1).getId());
    }
}
