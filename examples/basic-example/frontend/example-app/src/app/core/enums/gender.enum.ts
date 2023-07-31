export enum Gender {
  Male = 'M',
  Female = 'F',
  Other = 'O',
}

export const GenderMap = {
  [Gender.Male]: {
    id: Gender.Male,
    name: 'Male',
  },
  [Gender.Female]: {
    id: Gender.Female,
    name: 'Female',
  },
  [Gender.Other]: {
    id: Gender.Other,
    name: 'Other',
  },
};
