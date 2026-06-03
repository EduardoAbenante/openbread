import Card from "../../components/common/Card";
import Grid from "../../components/common/Grid";
import Section from "../../components/common/Section";
import "./dashboard.css";

export default function DashboardPage() {
  return (
    <div className="dashboard-container">
      <Section title="Resumen general">
        <Grid columns={3}>
          <Card title="Operarios activos">
            <p> 12 operatios </p>
          </Card>

          <Card title="Clientes registrados">
            <p> 58 clientes </p>
          </Card>

          <Card title="Productos en catálogo">
            <p> 143 productos</p>
          </Card>
        </Grid>
      </Section>

      <Section title="Actividad reciente">
        <Card>
          <p> PLACEHOLDER</p>
        </Card>
      </Section>
    </div>
  )
}