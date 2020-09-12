import store from '@/store';
import { SelectItem } from '@/common/types';
import dayjs from 'dayjs';
import _ from 'lodash';

export function getMemberNm(value: string | undefined | null): string {
  const find: SelectItem = store.state.cache.members.find(
    (value1: SelectItem) => value1!.value === value,
  );
  return find?.text || value || '';
}

export function formatDatetime(
  value: string | number | Date | undefined | null,
) {
  if (value === undefined || value === null || value === '') {
    return '';
  }
  return dayjs(value).format('YYYY-MM-DD HH:mm:ss');
}

export function formatDate(value: string | number | Date | undefined | null) {
  if (value === undefined || value === null || value === '') {
    return '';
  }
  return dayjs(value).format('YYYY-MM-DD');
}

export function getEllipseText(
  value: string | null | undefined,
  length: number,
) {
  if (value === undefined || value === null || value === '') {
    return '';
  }
  return _.truncate(value, {
    length: length,
  });
}

export function getSwitchLabel(yn: boolean, prefix?: string) {
  return _.trim((prefix || '') + (yn ? ' 사용' : ' 사용안함'));
}

export function textEllipsis(): void {
  document
    .querySelectorAll<HTMLElement>('.text-ellipsis-target')
    .forEach((item) => item.classList.remove('text-ellipsis'));
  setTimeout(() => {
    document
      .querySelectorAll<HTMLElement>('.text-ellipsis-target')
      .forEach((item) => {
        item.style.maxWidth = item.offsetWidth + 'px';
        item.classList.add('text-ellipsis');
      });
  }, 200);
}
