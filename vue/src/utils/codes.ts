import { SelectItem } from '@/common/types';
import _ from 'lodash';

export function getCodes(
  codes: SelectItem[],
): {
  text: string;
  value: string;
}[] {
  return Object.values(codes);
}

export function getText(
  codes: SelectItem[] | null,
  value: string,
  defaultText?: string,
): string {
  const filterElement = (codes || []).find((item) => item.value === value);
  return (filterElement && filterElement.text) || defaultText || '-';
}

export function unescapeXss(input: string) {
  const ta = document.createElement('textarea');
  ta.innerHTML = input; // vulnerable in ie 9 and firefox
  return ta.value;
}

export function getAlarmLevelColor(alarmLevel: string): string | undefined {
  if (alarmLevel === 'CRI') {
    return 'red darken-4';
  } else if (alarmLevel === 'MAJ') {
    return 'yellow darken-4';
  } else if (alarmLevel === 'MIN') {
    return 'lime darken-1';
  } else if (alarmLevel === 'INFO') {
    return 'grey darken-1';
  }
  return undefined;
}
