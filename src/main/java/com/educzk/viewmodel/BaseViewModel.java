package com.educzk.viewmodel;

import org.zkoss.zul.Messagebox;

public abstract class BaseViewModel {

    protected void executar(Runnable acao, String mensagemSucesso) {
        try {
            acao.run();
            Messagebox.show(mensagemSucesso, "Tudo certo", Messagebox.OK, Messagebox.INFORMATION);
        } catch (RuntimeException exception) {
            Messagebox.show(exception.getMessage(), "Atenção", Messagebox.OK, Messagebox.EXCLAMATION);
        }
    }
}
