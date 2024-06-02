import {
  Admin,
  Resource,
  ListGuesser,
  EditGuesser,
  ShowGuesser,
} from "react-admin";
import { dataProvider } from "./dataProvider";
import {EnterpriseList} from "./enterprises";



export const App = () => (
  <Admin dataProvider={dataProvider}>
    <Resource
      name="enterprises"
      list={EnterpriseList}
      edit={EditGuesser}
      show={ShowGuesser}
    />
    <Resource
      name="denominations"
      list={ListGuesser}
      edit={EditGuesser}
      show={ShowGuesser}
    />
    <Resource
      name="establishments"
      list={ListGuesser}
      edit={EditGuesser}
      show={ShowGuesser}
    />
  </Admin>
);
