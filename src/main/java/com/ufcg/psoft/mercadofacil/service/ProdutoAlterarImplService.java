package com.ufcg.psoft.mercadofacil.service;

import com.ufcg.psoft.mercadofacil.model.Produto;
import com.ufcg.psoft.mercadofacil.repository.ProdutoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ProdutoAlterarImplService implements ProdutoAlterarService {
    @Autowired
    ProdutoRepository<Produto, Long> produtoRepository;
    @Override
    public Produto alterar(Produto produtoAlterado) {

        if(produtoAlterado.getNome() == null || produtoAlterado.getCodigoBarra() == null
                || produtoAlterado.getFabricante() == null) {
            throw new RuntimeException("Atributos faltando!");
        }

        if(produtoAlterado.getPreco()<=0) {
            throw new RuntimeException("Preco invalido!");
        }

        String codigoBarra = produtoAlterado.getCodigoBarra();
        if (codigoBarra.length() > 13) {
            throw new RuntimeException("Codigo de barra com mais de 13 numeros!");
        }
        int impar = 0, par = 0;

        for (int i = 0; i < 12; i++) {
            if (i % 2 == 0) {
                par += Integer.parseInt(String.valueOf(codigoBarra.charAt(i)));
            } else {
                impar += Integer.parseInt(String.valueOf(codigoBarra.charAt(i)));
            }
        }


        int verificador = 0;
        int calculo = (par * 3) + impar;

        while ((calculo + verificador) % 10 != 0) {
            verificador++;
        }
        if (verificador != Integer.parseInt(String.valueOf(codigoBarra.charAt(12)))) {
            throw new RuntimeException("Codigo de barra com digito verificador incorreto!");
        }

        return produtoRepository.update(produtoAlterado);
    }

}

