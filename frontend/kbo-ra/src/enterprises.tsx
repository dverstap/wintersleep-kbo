import {Datagrid, DateField, List, TextField, TextInput} from 'react-admin';

const filters = [
    <TextInput source="q" label="Search" alwaysOn/>,
    //<ReferenceInput source="userId" label="User" reference="users" />,
];

export const EnterpriseList = () => (
    <List filters={filters}>
        <Datagrid rowClick="edit">
            <TextField source="id"/>
            <TextField source="type"/>
            <DateField source="startDate"/>
            <TextField source="status"/>
        </Datagrid>
    </List>
);
