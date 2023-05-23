package dao;

import model.Tarefa;
import util.EntityManagerFactoryUtil;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceException;
import jakarta.persistence.Query;

import java.util.ArrayList;
import java.util.List;

public class TarefaDAO {

    public void salvar(Tarefa tarefa) {
        EntityManager em = EntityManagerFactoryUtil.getEntityManagerFactory().createEntityManager();
        try {
            em.getTransaction().begin();
            em.persist(tarefa);
            em.getTransaction().commit();
        } catch (PersistenceException p) {
            System.out.println(p);
        } finally {
            em.close();
        }
    }

    public List<Tarefa> findAllByIdTituloDescricaoResponsavelStatus(Long id, String tituloDescicao, String responsavel, Boolean status) {
        EntityManager em = EntityManagerFactoryUtil.getEntityManagerFactory().createEntityManager();
        List<Tarefa> tfs = new ArrayList();
        try {
            em.getTransaction().begin();
            StringBuilder jpql = new StringBuilder();
            jpql.append(" select t ");
            jpql.append(" from Tarefa t ");
            jpql.append(" where ");
            jpql.append("   t.status = :status ");
            if (tituloDescicao != null && !tituloDescicao.isEmpty()) {
                jpql.append("   and ( unaccent(upper(t.titulo )) like unaccent(upper(:tituloDescicao)) or unaccent(upper(t.descricao)) like unaccent(upper(:tituloDescicao))) ");
            }
            if (responsavel != null && !responsavel.isEmpty()) {
                jpql.append("   and t.responsavel = :responsavel ");
            }
            if (id != null && id >= 0) {
                jpql.append("   and  t.id = :id ");
            }

            Query q = em.createQuery(jpql.toString());
            if (id != null && id >= 0) {
                q.setParameter("id", id);
            }
            if (tituloDescicao != null && !tituloDescicao.isEmpty()) {
                q.setParameter("tituloDescicao", "%" + tituloDescicao + "%");
            }
            if (responsavel != null && !responsavel.isEmpty()) {
                q.setParameter("responsavel", responsavel);
            }
            q.setParameter("status", status);

            tfs = q.getResultList();
            em.getTransaction().commit();
        } catch (Exception e) {
            if (em != null && em.getTransaction().isActive()) {
                em.getTransaction().rollback();
            }
            System.out.println(e);
        } finally {
            em.close();
        }
        return tfs;
    }

    public void remover(Tarefa tarefa) {
        EntityManager em = EntityManagerFactoryUtil.getEntityManagerFactory().createEntityManager();
        try {
            em.getTransaction().begin();
            em.remove(em.find(Tarefa.class,tarefa.getId()));
            em.getTransaction().commit();
        } catch (PersistenceException p) {
            if (em != null && em.getTransaction().isActive()) {
                em.getTransaction().rollback();
            }
            System.out.println(p);
        } finally {
            em.close();
        }
    }

    public void alterar(Tarefa tarefa) {
        EntityManager em = EntityManagerFactoryUtil.getEntityManagerFactory().createEntityManager();
        try {
            em.getTransaction().begin();
            em.merge(tarefa);
            em.getTransaction().commit();
        } catch (PersistenceException p) {
            if (em != null && em.getTransaction().isActive()) {
                em.getTransaction().rollback();
            }
            System.out.println(p);
        } finally {
            em.close();
        }
    }

    public Tarefa findById(Long id) {
        EntityManager em = EntityManagerFactoryUtil.getEntityManagerFactory().createEntityManager();
        Tarefa tarefa = new Tarefa();
        try {
            String jpql = "SELECT t FROM Tarefa t WHERE t.id = :id";
            tarefa = em.createQuery(jpql, Tarefa.class)
                    .setParameter("id", id)
                    .getSingleResult();
        } catch (PersistenceException p) {
            System.out.println(p);
        } finally {
            em.close();
        }
        return tarefa;
    }
}



