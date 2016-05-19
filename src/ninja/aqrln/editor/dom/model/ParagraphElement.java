package ninja.aqrln.editor.dom.model;

/**
 * @author Alexey Orlenko
 */
public class ParagraphElement extends DocumentModelCompositeElement {
    private ParagraphAlignment alignment = ParagraphAlignment.JUSTIFY;

    public ParagraphAlignment getAlignment() {
        return alignment;
    }

    public void setAlignment(ParagraphAlignment alignment) {
        this.alignment = alignment;
    }

    @Override
    public void accept(DocumentModelVisitor visitor) {
        visitor.visitParagraphElement(this);
    }
}
