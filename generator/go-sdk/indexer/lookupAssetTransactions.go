package indexer

import (
	"context"
	"encoding/base64"
	"fmt"
	"time"

	"github.com/algorand/go-algorand-sdk/client/v2/common"
	"github.com/algorand/go-algorand-sdk/client/v2/common/models"
	"github.com/algorand/go-algorand-sdk/types"
)

// LookupAssetTransactions /v2/assets/{asset-id}/transactions
// Lookup transactions for an asset.
type LookupAssetTransactions struct {
	c       *Client
	p       models.LookupAssetTransactionsParams
	assetId uint64
}

// Address only include transactions with this address in one of the transaction
// fields.
func (s *LookupAssetTransactions) Address(address types.Address) *LookupAssetTransactions {
	s.p.Address = address.String()
	return s
}

// AddressRole combine with the address parameter to define what type of address to
// search for.
func (s *LookupAssetTransactions) AddressRole(addressRole string) *LookupAssetTransactions {
	s.p.AddressRole = addressRole
	return s
}

// AfterTime include results after the given time. Must be an RFC 3339 formatted
// string.
func (s *LookupAssetTransactions) AfterTime(afterTime time.Time) *LookupAssetTransactions {
	s.p.AfterTime = afterTime.Format(time.RFC3339)
	return s
}

// BeforeTime include results before the given time. Must be an RFC 3339 formatted
// string.
func (s *LookupAssetTransactions) BeforeTime(beforeTime time.Time) *LookupAssetTransactions {
	s.p.BeforeTime = beforeTime.Format(time.RFC3339)
	return s
}

// CurrencyGreaterThan results should have an amount greater than this value.
// MicroAlgos are the default currency unless an asset-id is provided, in which
// case the asset will be used.
func (s *LookupAssetTransactions) CurrencyGreaterThan(currencyGreaterThan uint64) *LookupAssetTransactions {
	s.p.CurrencyGreaterThan = currencyGreaterThan
	return s
}

// CurrencyLessThan results should have an amount less than this value. MicroAlgos
// are the default currency unless an asset-id is provided, in which case the asset
// will be used.
func (s *LookupAssetTransactions) CurrencyLessThan(currencyLessThan uint64) *LookupAssetTransactions {
	s.p.CurrencyLessThan = currencyLessThan
	return s
}

// ExcludeCloseTo combine with address and address-role parameters to define what
// type of address to search for. The close to fields are normally treated as a
// receiver, if you would like to exclude them set this parameter to true.
func (s *LookupAssetTransactions) ExcludeCloseTo(excludeCloseTo bool) *LookupAssetTransactions {
	s.p.ExcludeCloseTo = excludeCloseTo
	return s
}

// Limit maximum number of results to return.
func (s *LookupAssetTransactions) Limit(limit uint64) *LookupAssetTransactions {
	s.p.Limit = limit
	return s
}

// MaxRound include results at or before the specified max-round.
func (s *LookupAssetTransactions) MaxRound(maxRound uint64) *LookupAssetTransactions {
	s.p.MaxRound = maxRound
	return s
}

// MinRound include results at or after the specified min-round.
func (s *LookupAssetTransactions) MinRound(minRound uint64) *LookupAssetTransactions {
	s.p.MinRound = minRound
	return s
}

// Next the next page of results. Use the next token provided by the previous
// results.
func (s *LookupAssetTransactions) Next(next string) *LookupAssetTransactions {
	s.p.Next = next
	return s
}

// NotePrefix specifies a prefix which must be contained in the note field.
func (s *LookupAssetTransactions) NotePrefix(notePrefix []byte) *LookupAssetTransactions {
	s.p.NotePrefix = base64.StdEncoding.EncodeToString(notePrefix)
	return s
}

// RekeyTo include results which include the rekey-to field.
func (s *LookupAssetTransactions) RekeyTo(rekeyTo bool) *LookupAssetTransactions {
	s.p.RekeyTo = rekeyTo
	return s
}

// Round include results for the specified round.
func (s *LookupAssetTransactions) Round(round uint64) *LookupAssetTransactions {
	s.p.Round = round
	return s
}

// SigType filters just results using the specified type of signature:
// * sig - Standard
// * msig - MultiSig
// * lsig - LogicSig
func (s *LookupAssetTransactions) SigType(sigType string) *LookupAssetTransactions {
	s.p.SigType = sigType
	return s
}

func (s *LookupAssetTransactions) TxType(txType string) *LookupAssetTransactions {
	s.p.TxType = txType
	return s
}

// TxID lookup the specific transaction by ID.
func (s *LookupAssetTransactions) TxID(txID string) *LookupAssetTransactions {
	s.p.TxID = txID
	return s
}

// Do performs HTTP request
func (s *LookupAssetTransactions) Do(ctx context.Context,
	headers ...*common.Header) (response models.TransactionsResponse, err error) {
	err = s.c.get(ctx, &response,
		fmt.Sprintf("/v2/assets/%d/transactions", s.assetId), s.p, headers)
	return
}
