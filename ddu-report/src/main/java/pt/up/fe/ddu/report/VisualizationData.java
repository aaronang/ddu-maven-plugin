package pt.up.fe.ddu.report;

import pt.up.fe.ddu.base.model.Node;
import pt.up.fe.ddu.base.model.Tree;
import pt.up.fe.ddu.base.spectrum.Spectrum;

import java.util.List;

public class VisualizationData {
    private AbstractReport report;
    private Spectrum spectrum;

    public VisualizationData(AbstractReport report) {
        this.report = report;
        this.spectrum = report.getSpectrum();
    }

    public String serializeDDUData() {
        Tree t = spectrum.getTree();
        StringBuilder sb = new StringBuilder();
        sb.append("mydata = {");

        sb.append("\"tests\":[");
        for (int transaction = 0; transaction < spectrum.getTransactionsSize(); transaction++) {
            serializeTransaction(transaction, sb);
        }

        sb.append("],\"tree\":");

        Node n = t.getRoot();
        serializeNode(n, sb);

        sb.append("}");
        return sb.toString();
    }

    private void serializeNode(Node n, StringBuilder sb) {
        if (n.isLeaf()) {
            sb.append("{");
            sb.append(String.format("\"name\":\"%s\",", n.getShortName()));
            //probe id and score
            int id = spectrum.getProbeOfNode(n.getId());

            sb.append(String.format("\"cid\":%d", id));
            sb.append("}");
        }
        else {
            List<Node> children = n.getChildren();
            if (children.size() < 2 && children.get(0).getType() == Node.Type.PACKAGE) {
                serializeNode(children.get(0), sb);
            } else {
                FilteredReport fr = new FilteredReport(spectrum, report.granularity, n);
                double ddu = fr.getMetrics().get(3).calculate();

                sb.append("{");
                sb.append(String.format("\"name\":\"%s\",", n.getShortName()));
                sb.append(String.format("\"ddu\":\"%f\",", ddu));
                //children
                sb.append("\"children\":[");
                for (Node child : n.getChildren()) {
                    serializeNode(child, sb);
                    sb.append(",");
                }
                sb.append("]");
                sb.append("}");
            }
        }
    }

    private void serializeTransaction(int t, StringBuilder sb) {
        sb.append(spectrum.getActiveComponentsInTransaction(t).toString() + ",");
    }
}
