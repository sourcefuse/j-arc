export enum CalendarDayEnum {
  H = 'holiday',
  T = 'today',
  N = 'non-working-day',
  W = 'working-days',
}

export const CalendarDayClassMap = {
  [CalendarDayEnum.H]: 'holiday',
  [CalendarDayEnum.T]: 'today',
  [CalendarDayEnum.N]: 'off-day',
  [CalendarDayEnum.W]: 'working-day',
};
