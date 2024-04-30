package com.vrrom.util;

import com.vrrom.application.model.AgreementInfo;
import com.vrrom.util.exceptions.PdfGenerationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.thymeleaf.context.Context;
import org.thymeleaf.spring6.SpringTemplateEngine;
import org.xhtmlrenderer.pdf.ITextRenderer;

import java.io.ByteArrayOutputStream;

@Service
public class PdfGenerator {
    private final SpringTemplateEngine templateEngine;

    @Autowired
    public PdfGenerator(SpringTemplateEngine templateEngine) {
        this.templateEngine = templateEngine;
    }

    public byte[] generateAgreement(AgreementInfo agreementInfo) throws PdfGenerationException {
        try {
            Context context = new Context();
            context.setVariable("agreement", agreementInfo);
            String processedHtml = templateEngine.process("agreement", context);
            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            ITextRenderer renderer = new ITextRenderer();
            renderer.setDocumentFromString(processedHtml);
            renderer.layout();
            renderer.createPDF(outputStream, false);
            renderer.finishPDF();
            return outputStream.toByteArray();
        } catch (Exception e) {
            throw new PdfGenerationException("Failed to generate PDF", e.getCause());
        }
    }
}

